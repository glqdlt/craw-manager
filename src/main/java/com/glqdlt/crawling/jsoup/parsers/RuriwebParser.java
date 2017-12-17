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
public class RuriwebParser extends ParserUtill implements Callable<List<CrawRawDataEntity>> {

	private static final Logger log = LoggerFactory.getLogger(RuriwebParser.class);

	
	@Autowired
	CrawDataService cService;
	private CrawDomainEntity cDomain;

	public RuriwebParser(CrawDomainEntity cDomain) {
		this.cDomain = cDomain;
	}

	@Override
	public List<CrawRawDataEntity> startJob(CrawDomainEntity cDomain) {
		List<CrawRawDataEntity> list = new ArrayList<CrawRawDataEntity>();

		int lastBoardNo = 1;

		String subject = null;
		String boardWriteDate = null;
		String link = null;
		String boardNo = null;
		try {
			Document doc = Jsoup.connect(cDomain.getUrl()).get();
			Elements elemnts = doc.getElementsByClass("board_list_table");
			elemnts = elemnts.get(0).getElementsByClass("table_body");

			for (Element el : elemnts) {
				Elements elements2 = el.getElementsByClass("table_body");
				for (Element el2 : elements2) {
					if (el2.className().equals("table_body")) {
						CrawRawDataEntity crawObj = new CrawRawDataEntity();
						link = el.getElementsByClass("subject").get(0).getElementsByClass("deco").attr("href");
						boardWriteDate = el.getElementsByClass("time").text();
						subject = el.getElementsByClass("subject").text();
						subject = FindReply(subject);
						boardNo = el.getElementsByClass("id").text();

						crawObj.setBoardWriteDate(boardWriteDate);
						crawObj.setLink(link);
						crawObj.setSubject(subject);
						crawObj.setBoardNo(parserBoardNo(boardNo));

						setCommonData(crawObj, cDomain);

						if (lastBoardNo < Integer.parseInt(boardNo)) {
							list.add(crawObj);
						}
						crawObj = null;
					}

				}
			}

		} catch (IOException e) {
			log.error("Parser Error.." + e);
		}
		return list;
	}

	private String FindReply(String text) {

		text = text.trim();
		if (text.substring(text.length() - 1, text.length()).equals("N")) {
			text = text.substring(0, text.length() - 2);
		}
		text = text.trim();

		if (text.lastIndexOf(")") == (text.length()) - 1) {

			text = text.substring(0, text.lastIndexOf("(") - 1);

		}

		return text;
	}

	@Override
	public List<CrawRawDataEntity> call() throws Exception {
		return startJob(cDomain);
	}

}
