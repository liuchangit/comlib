package com.liuchangit.comlib.mail;

import org.junit.Test;

import com.liuchangit.comlib.mail.MailSender;

public class MailSenderTest {
	
	@Test
	public void testSendMail() throws Exception {
		MailSender.send("test");
		MailSender.send("test", "just a test");
		MailSender.send("test", "just a test from charles_liu@staff.easou.com", "charles_liu@staff.easou.com");
	}

}
