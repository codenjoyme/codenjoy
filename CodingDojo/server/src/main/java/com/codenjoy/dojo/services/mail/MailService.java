package com.codenjoy.dojo.services.mail;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailService {

    public void sendEmail(String to, String title, String body) throws MessagingException {
        final String userName = "info@codenjoy.com";
        final String password = "UT4%-c0=gX=$";
        String port = "465";

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.host", "gc2.nodecluster.net");
//        props.put("mail.smtp.host", "mail.codenjoy.com");
//        props.put("mail.smtp.port", "26");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.ssl.trust", "gc2.nodecluster.net");
        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.password", password);
        props.put("mail.user", userName);
        props.put("mail.from", userName);
        props.put("mail.smtp.localhost", "codenjoy.com");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);

        // Get the default Session object.
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
//        session.setProtocolForAddress("rfc822", "smtps");

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title);
        message.setContent(body, "text/html; charset=utf-8");
        Transport.send(message);
    }


}
