package com.glqdlt.crawling.jsoup.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.crawling.jsoup.utill.ParserUtill;
import com.glqdlt.persistence.entity.CrawDomainEntity;
import com.glqdlt.persistence.service.CrawDataService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class CoolenjoyNewsParser extends ParserUtill implements Callable<List<CrawRawDataEntity>> {

	private static final Logger log = LoggerFactory.getLogger(CoolenjoyNewsParser.class);

	@Autowired
	CrawDataService cService;
	private CrawDomainEntity cDomain;

	public CoolenjoyNewsParser(CrawDomainEntity cDomain) {
		this.cDomain = cDomain;

	}

	@Override
	public List<CrawRawDataEntity> startJob(CrawDomainEntity cDomain) {

		int lastBoardNo = 1;
		String subject = null;
		String link = null;
		String date = null;
		String boardNo = null;

		List<CrawRawDataEntity> list = new ArrayList<CrawRawDataEntity>();
		try {
			Document doc = Jsoup.connect(cDomain.getUrl()).get();
			Elements trElements = doc.getElementsByTag("table").get(0).getElementsByTag("tr");

			for (Element tr : trElements) {
				CrawRawDataEntity crawObj = new CrawRawDataEntity();
				Elements tds = tr.getElementsByTag("td");

				if (tds.hasClass("td_subject")) {

					for (Element element2 : tds) {

						if (element2.className().equals("td_subject")) {
							subject = element2.text();
							link = element2.getElementsByTag("a").get(0).attr("href");
							boardNo = link.substring((link.lastIndexOf("/") + 1), link.length());

						}
						if (element2.className().equals("td_date")) {
							date = element2.text();

						}

					}

					subject = subjectRegex(subject);

					crawObj.setSubject(subject);
					crawObj.setBoardWriteDate(date);
					crawObj.setLink(link);
					crawObj.setBoardNo(parserBoardNo(boardNo));
					
					setCommonData(crawObj, cDomain);

					if (lastBoardNo < Integer.parseInt(boardNo)) {
						list.add(crawObj);
					}
				}
				crawObj = null;

			}

		} catch (IOException e) {
			log.error("Parser Error.." + e);
		}
		return list;
	}

	private String subjectRegex(String text) {

		if (text.lastIndexOf("개") == (text.length()) - 1) {
			text = text.substring(0, text.lastIndexOf("댓글"));
		}
		;

		return text;
	}

	@Override
	public List<CrawRawDataEntity> call() throws Exception {

		return startJob(cDomain);
	}

}
