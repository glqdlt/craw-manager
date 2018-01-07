package com.glqdlt.crawling.jsoup.utill;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.persistence.entity.CrawDomainEntity;

//@Component
public abstract class ParserUtill {

	private static final Logger log = LoggerFactory.getLogger(ParserUtill.class);

	public abstract List<CrawRawDataEntity> startJob(CrawDomainEntity cDomain);
	
	
	protected void checkNewHash() {

		Document document = null;
		String oldHash = null;
		String newHash = null;

		try {
			document = Jsoup.connect("url").get();
			newHash = checkMD5(document.toString());
		} catch (IOException e) {
			log.error("checkMD5 Error." + e);
		}

	}

	private boolean equalsHash(String newMd5, String oldMd5) {

		if (!(oldMd5.equals(newMd5))) {

			return true;
		}
		return false;

	}

	private String checkMD5(String text) {
		String md5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : byteData) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}

			md5 = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			md5 = null;
		}
		return md5;
	}

	protected String GetToday() {

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy_MM_dd");
		return fm1.format(new Date());
	}

	protected void setCommonData(CrawRawDataEntity crawRawDataEntity, CrawDomainEntity crawDomainEntity) {
		crawRawDataEntity.setDataName(crawDomainEntity.getDataName());
		crawRawDataEntity.setDataTag(crawDomainEntity.getDataTag());
		crawRawDataEntity.setSiteName(crawDomainEntity.getSiteName());
		crawRawDataEntity.setSiteTag(crawDomainEntity.getSiteTag());
		crawRawDataEntity.setCrawNo(crawDomainEntity.getCrawNo());
		crawRawDataEntity.setCreatedTime(new Date());
	}
	
	protected Integer parserBoardNo(String boardNo){
		
		return Integer.parseInt(boardNo);
	}

}
