package com.codenjoy.dojo.services.mail;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailService {

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.name}")
    private String emailName;

    public void sendEmail(String to, String title, String body) throws MessagingException {
        if (StringUtils.isEmpty(emailName)) {
            return;
        }
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
        props.put("mail.smtp.user", emailName);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.password", emailPassword);
        props.put("mail.user", emailName);
        props.put("mail.from", emailName);
        props.put("mail.smtp.localhost", "codenjoy.com");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);

        // Get the default Session object.
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailName, emailPassword);
            }
        });
//        session.setProtocolForAddress("rfc822", "smtps");

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title);
        message.setContent(body, "text/html; charset=utf-8");
        Transport.send(message);
    }


}
