package com.glqdlt.mail.gmail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.glqdlt.mail.MailBodyManager;
import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.persistence.entity.UserEntity;

public class GMailSender {

	protected String GetToday() {

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy년 MM월 dd일");
		return fm1.format(new Date());
	}

	public static void SendGMail_Group(GMailBuilder build, List<String> group_list) {
		try {
			Message msg = MessageSessionMaker(build);

			InternetAddress[] ToUsers = new InternetAddress[group_list.size()];

			for (int i = 0; i < ToUsers.length; i++) {
				ToUsers[i] = new InternetAddress(group_list.get(i));
			}
			msg.setRecipients(Message.RecipientType.TO, ToUsers);

			Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void SendGMail(GMailBuilder build, String to_user, String subject) {
		try {
			Message msg = MessageSessionMaker(build);

			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to_user));
			msg.setSubject(subject);

			Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void MailSend(List<List<CrawRawDataEntity>> list, List<UserEntity> to_user_list) {

		GMailBuilder build = new GMailBuilder();

		String html = MailBodyManager.HtmlBuilder(MailBodyManager.BodyMaker(list));
		build.setMail_body(html);

		int new_crawlling_data_raw_count = 0;
		for (List<CrawRawDataEntity> l : list) {
			new_crawlling_data_raw_count += l.size();

		}

		String subject = "";
		for (UserEntity MVO : to_user_list) {
			subject = "[행운의떼껄룩] 안녕하세요, " + MVO.getName() + " 님. 신규 정보 '" + new_crawlling_data_raw_count
					+ "'개 있습니다. 확인하세요! ";
			GMailSender.SendGMail(build, MVO.getEmail(), subject);
		}
		subject = null;

	}

	private static Message MessageSessionMaker(GMailBuilder build) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", GMailConfig.Prop.getHost_name());
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", GMailConfig.Prop.getPort());
		props.put("mail.smtp.socketFactory.port", GMailConfig.Prop.getPort());
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(GMailConfig.Prop.Get_Uid(), GMailConfig.Prop.Get_Upw());
			}
		});

		session.setDebug(true);

		Message msg = new MimeMessage(session);
		InternetAddress FromUser = new InternetAddress(GMailConfig.Prop.Get_Mail());
		msg.setFrom(FromUser);

		msg.setContent(build.getMail_body(), GMailConfig.Prop.getMaiL_char());

		return msg;

	}

}
