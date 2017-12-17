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
public class CoolenjoyTukgaParser extends ParserUtill implements Callable<List<CrawRawDataEntity>> {

	private static final Logger log = LoggerFactory.getLogger(CoolenjoyTukgaParser.class);

	@Autowired
	CrawDataService cService;
	private CrawDomainEntity cDomain;

	public CoolenjoyTukgaParser(CrawDomainEntity cDomain) {
		this.cDomain = cDomain;
	}

	@Override
	public List<CrawRawDataEntity> startJob(CrawDomainEntity cDomain) {

		List<CrawRawDataEntity> list = new ArrayList<CrawRawDataEntity>();
		int lastBoardNo = 1;
		String subject = null;
		String fullBody = null;
		// String salePariod = null;
		String link = null;
		// String price = null;
		String boardNo = null;
		try {
			Document doc = Jsoup.connect(cDomain.getUrl()).get();
			Elements tbodys = doc.getElementsByTag("tbody");
			Element tbody = tbodys.get(0);
			Elements trs = tbody.getElementsByTag("tr");
			for (Element element : trs) {
				CrawRawDataEntity crawObj = new CrawRawDataEntity();
				link = element.getElementsByClass("td_num").get(0).getElementsByTag("a").get(0).attr("href").toString();
				// price = element.getElementsByClass("td_won").text();

				fullBody = element.getElementsByClass("td_subject").text();
				fullBody = fullBody.replace("마감 |", "");
				fullBody = fullBody.replace("예정 |", "");
				fullBody = fullBody.replace("진행 |", "").trim();

				subject = fullBody.substring(0, (fullBody.lastIndexOf("댓글")));
				// salePariod = fullBody.substring(fullBody.indexOf("일정:") + 3,
				// (fullBody.length()));
				boardNo = link.substring((link.lastIndexOf("/") + 1), link.length());
				// CrawVO.setSale_pariod(salePariod);
				crawObj.setLink(link);
				crawObj.setSubject(subject);
				crawObj.setBoardNo(parserBoardNo(boardNo));

				setCommonData(crawObj, cDomain);

				if (lastBoardNo < Integer.parseInt(boardNo)) {
					list.add(crawObj);
				}

				crawObj = null;
			}

		} catch (IOException e) {
			log.error("Parser Error.." + e);
		}
		return list;
	}

	@Override
	public List<CrawRawDataEntity> call() throws Exception {
		return startJob(cDomain);
	}

}
