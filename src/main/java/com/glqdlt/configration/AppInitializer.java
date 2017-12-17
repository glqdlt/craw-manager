package com.glqdlt.configration;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.glqdlt.crawlling.service.TestModeManager;
import com.glqdlt.persistence.entity.CrawDomainEntity;
import com.glqdlt.persistence.repository.CrawDomainRepository;

@Service
public class AppInitializer {

	@Autowired
	CrawDomainRepository cDomainRepo;

	@Autowired
	JobManager cJobManager;

	@Autowired
	TestModeManager tModeManager;

	private static final Logger log = LoggerFactory.getLogger(AppInitializer.class);

	public void initCrawDomainDatas() {

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

	public void startTestMode() {
		if (JobStatus.getInstance().getTestMode() == 1) {
			tModeManager.start();
		}
	}

	private ArrayList<CrawDomainEntity> setUp() {

		ArrayList<CrawDomainEntity> list = new ArrayList<>();
		list.add(new CrawDomainEntity(1, Ruriweb.target_url, Ruriweb.data_name, Ruriweb.data_tag, Ruriweb.site_name,
				Ruriweb.site_tag, Ruriweb.craw_type, 1));
		list.add(new CrawDomainEntity(2, CoolenjoyNews.target_url, CoolenjoyNews.data_name, CoolenjoyNews.data_tag,
				CoolenjoyNews.site_name, CoolenjoyNews.site_tag, CoolenjoyNews.craw_type, 1));
		list.add(new CrawDomainEntity(3, CoolenjoyTukga.target_url, CoolenjoyTukga.data_name, CoolenjoyTukga.data_tag,
				CoolenjoyTukga.site_name, CoolenjoyTukga.site_tag, CoolenjoyTukga.craw_type, 1));
		list.add(new CrawDomainEntity(4, PpompuCoupon.target_url, PpompuCoupon.data_name, PpompuCoupon.data_tag,
				PpompuCoupon.site_name, PpompuCoupon.site_tag, PpompuCoupon.craw_type, 1));
		list.add(new CrawDomainEntity(5, PpompuTukga.target_url, PpompuTukga.data_name, PpompuTukga.data_tag,
				PpompuTukga.site_name, PpompuTukga.site_tag, PpompuTukga.craw_type, 1));
		list.add(new CrawDomainEntity(6, YepannetTukga.target_url, YepannetTukga.data_name, YepannetTukga.data_tag,
				YepannetTukga.site_name, YepannetTukga.site_tag, YepannetTukga.craw_type, 1));
		list.add(new CrawDomainEntity(7, YepannetYepan.target_url, YepannetYepan.data_name, YepannetYepan.data_tag,
				YepannetYepan.site_name, YepannetYepan.site_tag, YepannetYepan.craw_type, 1));
		return list;

	}

}
