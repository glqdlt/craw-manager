package com.glqdlt.crawling.jsoup.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class YepannetParser extends ParserUtill implements Callable<List<CrawRawDataEntity>> {

	@Autowired
	CrawDataService cService;

	private CrawDomainEntity cDomain;

	private static final Logger log = LoggerFactory.getLogger(YepannetParser.class);

	public YepannetParser(CrawDomainEntity cDomain) {
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
			Elements el = doc.select("tr[align=center]");
			for (Element element : el) {
				CrawRawDataEntity crawObj = new CrawRawDataEntity();

				link = element.getElementsByClass("mw_basic_list_thumb").select("a[href]").attr("href");

				subject = element.getElementsByClass("mw_basic_list_subject").text();
				subject = subjectRegex(subject);
				boardNo = boardNoRegex(link);
				boardWriteDate = element.getElementsByClass("mw_basic_list_datetime").text();

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

		} catch (IOException e) {
			log.error("Parser Error.." + e);
		}
		return list;
	}

	private String boardNoRegex(String link) {
		String regex = "(wr_id=)[0-9]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(link);
		m.find();
		String result = m.group();
		try {
			result = result.replace("wr_id=", "");
		} catch (IllegalStateException igone) {
			result = "0";
		}
		return result;
	}

	private String subjectRegex(String subject) {
		String regex = "[+][0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(subject);
		if (m.find()) {
			String find = m.group();
			subject = subject.replace(find, "");
		}
		subject = subject.replace("[특판정보]", "");
		subject = subject.replace("[예판정보]", "");
		return subject;
	}

	@Override
	public List<CrawRawDataEntity> call() throws Exception {
		return startJob(cDomain);
	}
}
