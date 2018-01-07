package com.glqdlt.configration;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.glqdlt.crawling.domain.CoolenjoyNews;
import com.glqdlt.crawling.domain.CoolenjoyTukga;
import com.glqdlt.crawling.domain.PpompuCoupon;
import com.glqdlt.crawling.domain.PpompuTukga;
import com.glqdlt.crawling.domain.Ruriweb;
import com.glqdlt.crawling.domain.YepannetTukga;
import com.glqdlt.crawling.domain.YepannetYepan;
import com.glqdlt.crawlling.service.JobManager;
import com.glqdlt.crawlling.service.JobStatus;
import com.glqdlt.persistence.entity.CrawDomainEntity;
import com.glqdlt.persistence.repository.CrawDomainRepository;

@Service
public class AppInitializer {

	@Value("${craw.test.mode}")
	private boolean isTestMode;

	@Autowired
	CrawDomainRepository cDomainRepo;

	@Autowired
	JobManager cJobManager;

	private static final Logger log = LoggerFactory.getLogger(AppInitializer.class);

	public void initCrawDomainDatas() {

		if (isTestMode) {
			log.info("Test Mode On");
		} else {
			log.info("Not Test Mode");
		}

		setUp().forEach(x -> cDomainRepo.save(x));
		log.info("Init to Crawlling Domains.");

	}

	public void startAutoCrawlling(int delay) {
		if (JobStatus.getInstance().getStatus() != 1) {
			log.info("Start Crawlling Auto Mode.");
			cJobManager.setSleepTime(delay);
			cJobManager.start();
		}
	}

	private ArrayList<CrawDomainEntity> setUp() {

		ArrayList<CrawDomainEntity> list = new ArrayList<>();
		list.add(new CrawDomainEntity(1, Ruriweb.TARGET_URL, Ruriweb.DATA_NAME, Ruriweb.DATA_TAG, Ruriweb.SITE_NAME,
				Ruriweb.SITE_TAG, Ruriweb.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(2, CoolenjoyNews.TARGET_URL, CoolenjoyNews.DATA_NAME, CoolenjoyNews.DATA_TAG,
				CoolenjoyNews.SITE_NAME, CoolenjoyNews.SITE_TAG, CoolenjoyNews.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(3, CoolenjoyTukga.TARGET_URL, CoolenjoyTukga.DATA_NAME, CoolenjoyTukga.DATA_TAG,
				CoolenjoyTukga.SITE_NAME, CoolenjoyTukga.SITE_TAG, CoolenjoyTukga.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(4, PpompuCoupon.TARGET_URL, PpompuCoupon.DATA_NAME, PpompuCoupon.DATA_TAG,
				PpompuCoupon.SITE_NAME, PpompuCoupon.SITE_TAG, PpompuCoupon.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(5, PpompuTukga.TARGET_URL, PpompuTukga.DATA_NAME, PpompuTukga.DATA_TAG,
				PpompuTukga.SITE_NAME, PpompuTukga.SITE_TAG, PpompuTukga.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(6, YepannetTukga.TARGET_URL, YepannetTukga.DATA_NAME, YepannetTukga.DATA_TAG,
				YepannetTukga.SITE_NAME, YepannetTukga.SITE_TAG, YepannetTukga.CRAW_TYPE, 1));
		list.add(new CrawDomainEntity(7, YepannetYepan.TARGET_URL, YepannetYepan.DATA_NAME, YepannetYepan.DATA_TAG,
				YepannetYepan.SITE_NAME, YepannetYepan.SITE_TAG, YepannetYepan.CRAW_TYPE, 1));
		return list;

	}

}
