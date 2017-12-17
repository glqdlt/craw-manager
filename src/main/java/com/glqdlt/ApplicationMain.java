package com.glqdlt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.glqdlt.configration.AppInitializer;
import com.glqdlt.crawlling.service.JobStatus;

@ComponentScan(basePackages = "com.glqdlt.*")
@SpringBootApplication
public class ApplicationMain implements CommandLineRunner {

	@Autowired
	AppInitializer biService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		biService.initCrawDomainDatas();
		biService.startAutoCrawlling(60000);
		
		/**
		 * 0 == off;
		 * 1 == on;
		 */
		JobStatus.getInstance().setTestMode(0);
		biService.startTestMode();
		
	}

}
