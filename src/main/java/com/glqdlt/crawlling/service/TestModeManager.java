package com.glqdlt.crawlling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestModeManager extends Thread {
	
	
	private static final Logger log = LoggerFactory.getLogger(TestModeManager.class);


	@Autowired
	TestJobBroadCastingMode tJBCM;

	@Override
	public void run() {

		while(true){
		tJBCM.broadCasterTester();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		}

	}
}
