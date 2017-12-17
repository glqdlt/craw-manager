package com.glqdlt.crawlling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobManager extends Thread {

	@Autowired
	JobWorker jobWork;

	@Override
	public void run() {
		AutoCrawllingMode();
	}
	
	private Integer sleepTime;

	private static final Logger log = LoggerFactory.getLogger(JobManager.class);

	public void AutoCrawllingMode() {

		JobStatus.getInstance().setStatus(1);

		while (true) {
			log.debug("Start Auto Crawlling.");
			jobWork.jobRunner();
			log.debug("End Auto Crawlling");
			try {
				log.debug("Sleep..");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				log.error("Interpution ex", e);
			}
		}

	}

	public Integer getSleepTime() {
		return sleepTime;
	}

	synchronized public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

}
