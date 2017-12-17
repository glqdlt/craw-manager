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
public class PpompuParser extends ParserUtill implements Callable<List<CrawRawDataEntity>> {

	private static final Logger log = LoggerFactory.getLogger(PpompuParser.class);

	
	@Autowired
	CrawDataService cService;
	private CrawDomainEntity cDomain;

	public PpompuParser(CrawDomainEntity cDomain) {
		this.cDomain = cDomain;
	}

	@Override
	public List<CrawRawDataEntity> startJob(CrawDomainEntity cDomain) {
		List<CrawRawDataEntity> list = new ArrayList<CrawRawDataEntity>();

		int last_column_no = 1;

		String subject = null;
		String link = null;
		String boardNo = null;
		String href = null;
		Document doc = null;
		try {
			doc = Jsoup.connect(cDomain.getUrl()).get();
			Element table = doc.select("table[id=revolution_main_table]").get(0);
			Elements el = table.select("td[class=list_vspace]");
			for (Element element : el) {
				subject = (element.text());
				href = element.getElementsByTag("a").attr("href");
				if (href.equals("#")) {
					log.debug("find '#' break to continue.");
					continue;
				}
				CrawRawDataEntity crawObj = new CrawRawDataEntity();
				link = "http://www.ppomppu.co.kr/zboard/" + href;
				boardNo = boardNoRegex(link);

				crawObj.setLink(link);
				crawObj.setSubject(subject);
				crawObj.setBoardNo(parserBoardNo(boardNo));
				
				setCommonData(crawObj, cDomain);

				if (last_column_no < Integer.parseInt(boardNo)) {
					list.add(crawObj);
				}
			}
		} catch (IOException e) {
			log.error("Parser Error.." + e);
		}
		return list;

	}
	
	private String deleteReply(){
		return null;
	}

	private String boardNoRegex(String link) {
		String regex = "no(=)[0-9]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(link);
		m.find();
		String find = m.group();
		try {
			find = find.replace("no=", "");
		} catch (IllegalStateException igone) {
			find = "0";
		}
		return find;
	}

	@Override
	public List<CrawRawDataEntity> call() throws Exception {
		// TODO Auto-generated method stub
		return startJob(cDomain);
	}

}
