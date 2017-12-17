package com.glqdlt.mail.gmail;

public enum GMailConfig {

	Prop("mail@mail.com", "password", "mail@mail.com", "smtp.gmail.com", "465", "text/html; charset=UTF-8");

	private String uid;
	private String upw;
	private String mail;
	private String host_name;
	private String port;
	private String maiL_char;

	private GMailConfig(String uid, String upw, String mail, String host_name, String port, String mail_char) {
		this.uid = uid;
		this.maiL_char = mail_char;
		this.upw = upw;
		this.mail = mail;
		this.host_name = host_name;
		this.port = port;
	}

	public String Get_Uid() {
		return uid;
	}

	public String Get_Upw() {
		return upw;
	}

	public String Get_Mail() {
		return mail;
	}

	public String getHost_name() {
		return host_name;
	}

	public String getPort() {
		return port;
	}

	public String getMaiL_char() {
		return maiL_char;
	}

}
