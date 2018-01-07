package com.glqdlt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.glqdlt.configration.AppInitializer;
import com.glqdlt.crawlling.service.JobStatus;

@ComponentScan(basePackages = "com.glqdlt.*")
@SpringBootApplication
public class ApplicationMain implements CommandLineRunner {

	@Autowired
	AppInitializer appInitializer;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		appInitializer.initCrawDomainDatas();
		appInitializer.startAutoCrawlling(60000);
		
		/**
		 * 0 == off;
		 * 1 == on;
		 */
		JobStatus.getInstance().setTestMode(0);

	}

}
