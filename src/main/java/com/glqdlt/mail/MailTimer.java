package com.glqdlt.mail;

public class MailTimer {

	public static long GetMillsecond(int time, int minute, int second) {

		return (second * 1000) + (minute * 60000) + (time * 360000);

	}

}
