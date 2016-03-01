package com.liuchangit.comlib.mail;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.liuchangit.comlib.config.Config;
import com.liuchangit.comlib.config.PropertiesConfig;

public class MailSender {
	static Logger LOG = Logger.getLogger("common");
	
	static Config MAIL_CONFIG = new PropertiesConfig("mail");
	static ThreadPoolExecutor hitsExec = new ThreadPoolExecutor(3, 5, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(100));
	
	private static class SendThread extends Thread{
		private String subject;
		private String msg;
		private String from ;
		private String[]toList;
		private String[]ccList;
		private String[] bccList;
		public SendThread(String subject, String msg, String from, String[] toList, String[] ccList, String[] bccList) {
			this.subject = subject;
			this.msg = msg;
			this.from = from;
			this.toList = toList;
			this.ccList = ccList;
			this.bccList = bccList;
		}
		
		@Override
		public void run() {
			try{
				String smtpServer = MAIL_CONFIG.get("smtp.server");
				String auth = MAIL_CONFIG.get("auth");
				String username = MAIL_CONFIG.get("username");
				String password = MAIL_CONFIG.get("password");

				Properties mailProps = new Properties();
				mailProps.put("mail.smtp.host", smtpServer);
				mailProps.put("mail.smtp.auth", auth);
				mailProps.put("username", username);
				mailProps.put("password", password);

				Authenticator authenticator = new PopupAuthenticator(username, password);
				Session mailSession = Session.getInstance(mailProps, authenticator);
				MimeMessage message = new MimeMessage(mailSession);
				
				try {
					message.setSubject(subject);
					message.setFrom(new InternetAddress(from));
					if (toList != null && toList.length > 0) {
						for (String to : toList) {
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
						}
					}
					if (ccList != null && ccList.length > 0) {
						for (String cc : ccList) {
							message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
						}
					}
					if (bccList != null && bccList.length > 0) {
						for (String bcc : bccList) {
							message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
						}
					}
			
					MimeMultipart multi = new MimeMultipart();
					BodyPart textBodyPart = new MimeBodyPart();
					textBodyPart.setText(msg);
					multi.addBodyPart(textBodyPart);
					message.setContent(multi);
					message.saveChanges();
					Transport.send(message);
				} catch (Exception e) {
					LOG.error("MailSender.send fail", e);
				}
			}catch (Exception e) {
			}
		}
	}
	
	public static void send(String msg) {
		send(msg, msg);
	}
	
	public static void send(String msg, String[] toList) {
		send(msg, toList, MAIL_CONFIG.gets("cc"));
	}
	
	public static void send(String msg, String[] toList, String[] ccList) {
		send(msg, toList, ccList, MAIL_CONFIG.gets("bcc"));
	}
	
	public static void send(String msg, String[] toList, String[] ccList, String[] bccList) {
		send(msg, msg, toList, ccList, bccList);
	}
	
	public static void send(String subject, String msg) {
		send(subject, msg, MAIL_CONFIG.gets("to"));
	}
	
	public static void send(String subject, String msg, String to) {
		send(subject, msg, new String[] { to });
	}
	
	public static void send(String subject, String msg, String[] toList) {
		send(subject, msg, toList, MAIL_CONFIG.gets("cc"));
	}
	
	public static void send(String subject, String msg, String[] toList, String[] ccList) {
		send(subject, msg, toList, ccList, MAIL_CONFIG.gets("bcc"));
	}
	
	public static void send(String subject, String msg, String[] toList, String[] ccList, String[] bccList) {
		send(subject, msg, MAIL_CONFIG.get("from"), toList, ccList, bccList);
	}
	
	public static void send(String subject, String msg, String from, String to) {
		send(subject, msg, from, new String[] { to });
	}
	
	public static void send(String subject, String msg, String from, String[] toList) {
		send(subject, msg, from, toList, MAIL_CONFIG.gets("cc"));
	}
	
	public static void send(String subject, String msg, String from, String[] toList, String[] ccList) {
		send(subject, msg, from, toList, ccList, MAIL_CONFIG.gets("bcc"));
//		hitsExec.submit(new SendThread(subject,msg,from,toList,ccList,MAIL_CONFIG.gets("bcc")));
	}
	
	public static void send(String subject, String msg, String from, String[] toList, String[] ccList, String[] bccList) {
		Runnable task = new SendThread(subject,msg,from,toList,ccList,bccList);
		if (MAIL_CONFIG.getBool("async", true)) {
			hitsExec.submit(task);
		} else {
			task.run();
		}
	}
}

class PopupAuthenticator extends Authenticator {
	private String username;
	private String password;

	public PopupAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}

