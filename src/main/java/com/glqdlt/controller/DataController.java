package com.glqdlt.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.persistence.service.CrawDataService;
import com.glqdlt.persistence.vo.CrawDataCountVo;

@RequestMapping(value = "/craw")
@RestController
public class DataController {

	@Autowired
	CrawDataService cJobService;

	@RequestMapping(value = "/all")
	public List<CrawRawDataEntity> getDataAll() {

		List<CrawRawDataEntity> result = cJobService.getAllRawData();
		Collections.reverse(result);

		return result;
	}

	@RequestMapping(value = "/{page}", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getDataPage(@PathVariable int page){

		return new ResponseEntity<>(cJobService.getRawDataPage(page), HttpStatus.OK);
	}

	@RequestMapping(value = "/count/all")
	public CrawDataCountVo getCountAll() {

		CrawDataCountVo result = new CrawDataCountVo();

		result.setType("all");
		result.setResult(cJobService.getAllRawData().size());

		return result;
	}

}
