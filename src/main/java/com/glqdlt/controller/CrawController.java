package com.glqdlt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.glqdlt.crawlling.service.JobManager;
import com.glqdlt.crawlling.service.JobStatus;
import com.glqdlt.persistence.vo.CrawStatusVo;

@RequestMapping(value = "/crawlling/system")
@RestController
public class CrawController {

	@Autowired
	JobManager cJobManager;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "/status")
	public CrawStatusVo getStatus() {
		CrawStatusVo cObj = new CrawStatusVo();
		Integer status = 0;
		if (JobStatus.getInstance().getStatus() != 1) {
			status = 1;
		}
		cObj.setStatus(status);
		return cObj;
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "/shutdown")
	public void shutdown() {

	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "/restart")
	public void reStart() {

	}


}
