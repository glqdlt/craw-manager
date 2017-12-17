package com.glqdlt.system;

import java.util.List;

import com.glqdlt.persistence.vo.LastCrawDataVO;

public class LastCrawDataChecker {

	private List<LastCrawDataVO> list;
	private static LastCrawDataChecker ins;

	private LastCrawDataChecker() {

	}

	public static LastCrawDataChecker getIns() {
		if (ins == null) {
			ins = new LastCrawDataChecker();
		}
		return ins;
	}

	public List<LastCrawDataVO> getList() {
		return list;
	}

	public synchronized void setList(List<LastCrawDataVO> list) {
		this.list = list;
	}

	public String getLastHash(int site_tag, int data_tag) {
		String old_hash = "";
		for (LastCrawDataVO lvo : list) {
			if (lvo.getSiteTag() == site_tag) {
				if (lvo.getDataTag() == data_tag) {
					old_hash = lvo.getLastCrawHash();
				}
			}

		}
		return old_hash;
	}

	public int getLastColumn_no(int site_tag, int data_tag) {
		int old_column_no = 0;
		for (LastCrawDataVO lvo : list) {
			if (lvo.getSiteTag() == site_tag) {
				if (lvo.getDataTag() == data_tag) {
					old_column_no = lvo.getLastCrawNo();
				}
			}

		}
		return old_column_no;
	}

}
