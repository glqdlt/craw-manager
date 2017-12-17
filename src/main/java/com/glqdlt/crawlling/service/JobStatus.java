package com.glqdlt.crawlling.service;

public class JobStatus {

	private JobStatus() {
		status = 0;
	}

	private Integer status;
	private Integer testMode;

	public synchronized Integer getStatus() {
		return status;
	}

	public synchronized void setStatus(Integer status) {
		this.status = status;
	}

	public static JobStatus getInstance() {

		return LazyHolder.ins;
	}

	public synchronized Integer getTestMode() {
		return testMode;
	}

	public synchronized void setTestMode(Integer testMode) {
		this.testMode = testMode;
	}

	static class LazyHolder {

		public static final JobStatus ins = new JobStatus();

	}

}
