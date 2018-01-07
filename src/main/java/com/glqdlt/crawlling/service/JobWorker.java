package com.glqdlt.crawlling.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	CrawDataService crawDataService;

//	@Autowired
//	private SimpMessagingTemplate broker;

	private static final Logger log = LoggerFactory.getLogger(JobWorker.class);

	public void jobRunner() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<CrawDomainEntity> crawDomainEntityList = crawDataService.getAllCrawllingTargets();
		List<FutureDataVo> futureDataVoList = new ArrayList<>();

		for (CrawDomainEntity cTarget : crawDomainEntityList) {

			switch (cTarget.getCrawNo()) {

			case 1:
				Future<List<CrawRawDataEntity>> f1 = executorService.submit(new RuriwebParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f1, cTarget));
				break;

			case 2:
				Future<List<CrawRawDataEntity>> f2 = executorService.submit(new CoolenjoyNewsParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f2, cTarget));
				break;
			case 3:
				Future<List<CrawRawDataEntity>> f3 = executorService.submit(new CoolenjoyTukgaParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f3, cTarget));
				break;

			case 4:

				Future<List<CrawRawDataEntity>> f4 = executorService.submit(new PpompuParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f4, cTarget));
				break;

			case 5:

				Future<List<CrawRawDataEntity>> f5 = executorService.submit(new PpompuParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f5, cTarget));
				break;
			case 6:

				Future<List<CrawRawDataEntity>> f6 = executorService.submit(new YepannetParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f6, cTarget));
				break;
			case 7:

				Future<List<CrawRawDataEntity>> f7 = executorService.submit(new YepannetParser(cTarget));
				futureDataVoList.add(new FutureDataVo(f7, cTarget));
				break;
			default:
				break;
			}

		}

		for (FutureDataVo f : futureDataVoList) {
			List<CrawRawDataEntity> crawRawDataEntityList = newDataChecker(f);

			newDataSaver(crawRawDataEntityList);
			broadCaster(crawRawDataEntityList);
			
		}

	}

	public void broadCaster(List<CrawRawDataEntity> newCrawDataList) {
		if (newCrawDataList.size() == 0) {
			log.debug("broadCast to newcrawDataList is empty. escape.");
			return;
		}

		for (CrawRawDataEntity crawRawDataEntity : newCrawDataList) {
//			broker.convertAndSend("/push/newData", crawRawDataEntity);

//			 TODO websocket이 이제 api-gateway 에서 이루어지므로 apt-gateway 에 http api 로 찔러서 신규 데이터가 있음을 알려주는 template 만들기
			// TODO 데이터를 직접적으로 주는 것보단, 데이터베이스에 insert 까지 되는 것을 받아가는 것이 옮으으로 client 에서 최신 page =1 을 찔러오는 걸로 ( 중복되는 데이터가 다시 받을 수 있는 오버헤드가 있지만, 이게 구현이 편리함으로 이렇게 하기로 한다)
			// WEBSOCKET 은 신규 데이터를 알리기만 할 뿐, 신규 데이터가 있음을 알리는 sign을 받고 나면 client가 api-gate-way에 데이터를 달라고 찌르는 식으로.
			// 근데 이렇게 하면 위에서 말한 오버헤드도 있고, 신규 데이터를 new 라는 아이콘을 달아서 표기하는 데에 번거로워지는 게 생길 수 있다. 이걸 어떻게 할까

		}
	}

	private List<CrawRawDataEntity> newDataChecker(FutureDataVo futureDataVo) {

		CrawDomainEntity crawDomainEntity = futureDataVo.getCrawDomainEntity();
		Integer crawNo = crawDomainEntity.getCrawNo();
		Integer lastBoardNo = crawDataService.getLastBoardNo(crawNo);

		List<CrawRawDataEntity> crawRawDataEntityArrayList = new ArrayList<>();
		try {
			List<CrawRawDataEntity> crawRawDataEntityList = futureDataVo.getFuture().get(3, TimeUnit.MINUTES);
			// Order by Desc..
			Collections.reverse(crawRawDataEntityList);

			for (CrawRawDataEntity cRawData : crawRawDataEntityList) {
				if (cRawData.getBoardNo() > lastBoardNo) {
					crawRawDataEntityArrayList.add(cRawData);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception..", e);
		} catch (TimeoutException e) {
			log.error("timeOut..", e);
		}
		return crawRawDataEntityArrayList;
	}

	private void newDataSaver(List<CrawRawDataEntity> crawRawDataEntityList) {
		int findCount = crawRawDataEntityList.size();
		if (findCount == 0) {
			log.debug("Not found New Board");
			return;
		}

		for (CrawRawDataEntity crawRawDataEntity : crawRawDataEntityList) {
			log.debug("Find New Board, " + findCount + " : " + crawRawDataEntity.getSiteName() + "_"
					+ crawRawDataEntity.getDataName());
			crawDataService.saveCrawllingRawData(crawRawDataEntity);
		}

	}

}
