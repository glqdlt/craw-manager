package com.glqdlt.mail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.glqdlt.persistence.entity.CrawRawDataEntity;

public class MailBodyManager {
	
	
	final static String table = " <table border='1' style='margin: 5 px;padding: 0;border: #ccc 1px solid;outline: 0;font-size: 12px;vertical-align: baseline;background: #fff;border-spacing: 0;font-family: Arial, Helvetica, sans-serif;color: #666;text-shadow: 1px 1px 0 #fff;-moz-border-radius: 3px;-webkit-border-radius: 3px;border-radius: 3px;-moz-box-shadow: 0 1px 2px #d1d1d1;-webkit-box-shadow: 0 1px 2px #d1d1d1;box-shadow: 0 1px 2px #d1d1d1'>";
	final static String table2 = "</table>";
	final static String css = "<style type='text/css'>body {font-size:15pt;} </style>";
	final static String td = " <td style='margin: 0;padding: 18px;border: 0;outline: 0;font-size: 100%;vertical-align: baseline;background: -moz-linear-gradient(top, #fbfbfb, #fafafa);text-align: left;padding-left: 20px;border-left: 1px solid #e0e0e0;border-top: 1px solid #fff;border-bottom: 1px solid #e0e0e0'>";
	final static String td2 = "</td>";
	final static String th = "<th colspan='4' style='margin: 0;padding: 21px 25px 22px 25px;border: 0;outline: 0;font-size: 100%;vertical-align: baseline;background: -moz-linear-gradient(top, #ededed, #ebebeb);border-top: 1px solid #fafafa;border-bottom: 1px solid #e0e0e0;-moz-border-radius-topleft: 3px;-webkit-border-top-left-radius: 3px;border-top-left-radius: 3px;-moz-border-radius-topright: 3px;-webkit-border-top-right-radius: 3px;border-top-right-radius: 3px'>";
	final static String th2 = "</th>";
	final static String tr = "<tr style='margin: 0;padding: 0;border: 0;outline: 0;font-size: 100%;vertical-align: baseline;background: transparent;text-align: center;padding-left: 20px'>";
	final static String tr2 = "</tr>";
	final static String head = "<html><head></head>";
	final static String body = "<body style='height: 100%;margin: 0;padding: 0;border: 0;outline: 0;font-size: 15pt;vertical-align: baseline;background: transparent;line-height: 1'>";
	final static String tail = "</body></html>";

	
	/**
	 * 20170116 기간하고 가격은 쿨엔조이 특판에만 있어서 빼는게 좋지 않나 하는 생각으로 제거하기로 했다.
	 * @param list
	 * @return
	 */
	private static String HtmlTableMaker(List<CrawRawDataEntity> list) {

		String table_header = tr + th + list.get(0).getSiteName() + " " + list.get(0).getDataName() +" ("+list.size()+")"+ th2 + tr2;
		String table_index = tr +td+"순서(최신순)"+td2+  td + "제목" + td2 + tr2;

		String msg = "";

		int i = 0;
		for (CrawRawDataEntity c : list) {
			i++;
			msg += tr +td+i+td2+  td + "<a href='" + c.getLink() + "'>"
					+ CheckLength(c.getSubject()) + "</a>" + td2 +   tr2;

		}

		return table_header + table_index + msg;
	}

	public static String HtmlBuilder(String body) {

		return head + table + body + table2 + tail;
	}

	public static String BodyMaker(List<List<CrawRawDataEntity>> list) {

		String body = "";

		for (List<CrawRawDataEntity> CDVO : list) {
			/**
			 * 만약 신규 데이터가 없으면 pass 한다.
			 */
			if (CDVO.size() == 0) {
				continue;
			}
			body += HtmlTableMaker(CDVO);
		}

		return body;
	}

	private static String CheckLength(String text) {
		if (text.length() > 60) {
			text = text.substring(0, 58);
			text += " ...";
		}

		return text;

	}

	public static void FileWriter(String html, String path) {

		try {
			FileWriter fw = new FileWriter(new File(path));
			fw.append(html);
			fw.flush();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
