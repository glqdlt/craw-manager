package com.glqdlt.crawlling.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.glqdlt.persistence.entity.CrawRawDataEntity;

@Component
public class TestJobBroadCastingMode {

	@Autowired
	private SimpMessagingTemplate broker;

	private static final Logger log = LoggerFactory.getLogger(TestJobBroadCastingMode.class);

	public void jobRunner() {

		log.info("Send Test braodCaste");
		broadCasterTester();

	}

	public void broadCasterTester() {

		CrawRawDataEntity crawRawDataEntity = new CrawRawDataEntity();
		crawRawDataEntity.setNo(999);
		crawRawDataEntity.setSubject("test Subject");
		crawRawDataEntity.setSiteName("test");
		crawRawDataEntity.setCreatedTime(new Date());
		crawRawDataEntity.setBoardWriteDate("unknown");
		crawRawDataEntity.setLink("http://www.naver.com");
		broker.convertAndSend("/push/newData", crawRawDataEntity);
	}

}
