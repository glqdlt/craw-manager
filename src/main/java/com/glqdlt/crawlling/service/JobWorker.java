package com.glqdlt.crawlling.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.glqdlt.crawling.jsoup.parsers.CoolenjoyNewsParser;
import com.glqdlt.crawling.jsoup.parsers.CoolenjoyTukgaParser;
import com.glqdlt.crawling.jsoup.parsers.PpompuParser;
import com.glqdlt.crawling.jsoup.parsers.RuriwebParser;
import com.glqdlt.crawling.jsoup.parsers.YepannetParser;
import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.persistence.entity.CrawDomainEntity;
import com.glqdlt.persistence.service.CrawDataService;
import com.glqdlt.persistence.vo.FutureDataVo;

@Component
public class JobWorker {
	@Autowired
	CrawDataService cJobService;

	@Autowired
	private SimpMessagingTemplate broker;

	private static final Logger log = LoggerFactory.getLogger(JobWorker.class);

	public void jobRunner() {
		ExecutorService exePool = Executors.newCachedThreadPool();
		List<CrawDomainEntity> crawllingTargets = cJobService.getAllCrawllingTargets();
		List<FutureDataVo> fPool = new ArrayList<>();

		for (CrawDomainEntity cTarget : crawllingTargets) {

			switch (cTarget.getCrawNo()) {

			case 1:
				Future<List<CrawRawDataEntity>> f1 = exePool.submit(new RuriwebParser(cTarget));
				fPool.add(new FutureDataVo(f1, cTarget));
				break;

			case 2:
				Future<List<CrawRawDataEntity>> f2 = exePool.submit(new CoolenjoyNewsParser(cTarget));
				fPool.add(new FutureDataVo(f2, cTarget));
				break;
			case 3:
				Future<List<CrawRawDataEntity>> f3 = exePool.submit(new CoolenjoyTukgaParser(cTarget));
				fPool.add(new FutureDataVo(f3, cTarget));
				break;

			case 4:

				Future<List<CrawRawDataEntity>> f4 = exePool.submit(new PpompuParser(cTarget));
				fPool.add(new FutureDataVo(f4, cTarget));
				break;

			case 5:

				Future<List<CrawRawDataEntity>> f5 = exePool.submit(new PpompuParser(cTarget));
				fPool.add(new FutureDataVo(f5, cTarget));
				break;
			case 6:

				Future<List<CrawRawDataEntity>> f6 = exePool.submit(new YepannetParser(cTarget));
				fPool.add(new FutureDataVo(f6, cTarget));
				break;
			case 7:

				Future<List<CrawRawDataEntity>> f7 = exePool.submit(new YepannetParser(cTarget));
				fPool.add(new FutureDataVo(f7, cTarget));
				break;
			default:
				break;
			}

		}

		for (FutureDataVo f : fPool) {
			List<CrawRawDataEntity> newDataList = newDataChecker(f);

			newDataSaver(newDataList);
			broadCaster(newDataList);
			
		}

	}

	public void broadCaster(List<CrawRawDataEntity> newCrawDataList) {
		if (newCrawDataList.size() == 0) {
			log.debug("broadCast to newcrawDataList is empty. escape.");
			return;
		}

		for (CrawRawDataEntity crawRawDataEntity : newCrawDataList) {
			broker.convertAndSend("/push/newData", crawRawDataEntity);
		}
	}

	private List<CrawRawDataEntity> newDataChecker(FutureDataVo fObject) {

		CrawDomainEntity cDomain = fObject.getCDomain();
		Integer crawNo = cDomain.getCrawNo();
		Integer lastBoardNo = cJobService.getLastBoardNo(crawNo);

		List<CrawRawDataEntity> newCrawRawDatas = new ArrayList<>();
		try {
			List<CrawRawDataEntity> crawRawDatas = fObject.getFuture().get(3, TimeUnit.MINUTES);
			// Order by Desc..
			Collections.reverse(crawRawDatas);

			for (CrawRawDataEntity cRawData : crawRawDatas) {
				if (cRawData.getBoardNo() > lastBoardNo) {
					newCrawRawDatas.add(cRawData);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception..", e);
		} catch (TimeoutException e) {
			log.error("timeOut..", e);
		}
		return newCrawRawDatas;
	}

	private void newDataSaver(List<CrawRawDataEntity> newCrawRawData) {
		int findCount = newCrawRawData.size();
		if (findCount == 0) {
			log.debug("Not found New Board");
			return;
		}

		for (CrawRawDataEntity crawRawDataEntity : newCrawRawData) {
			log.debug("Find New Board, " + findCount + " : " + crawRawDataEntity.getSiteName() + "_"
					+ crawRawDataEntity.getDataName());
			cJobService.saveCrawllingRawData(crawRawDataEntity);
		}

	}

}
