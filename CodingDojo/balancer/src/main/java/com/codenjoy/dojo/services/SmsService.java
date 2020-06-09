package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.httpclient.SmsGatewayClient;
import com.codenjoy.dojo.services.properties.SmsProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    private static final String SEND_SMS_OPERATION = "SENDSMS";

    public enum SmsType {REGISTRATION, PASSWORD_RESET, NEW_PASSWORD}

    @Autowired private SmsProperties smsProperties;
    @Autowired private SmsGatewayClient gateway;

    public void sendSmsTo(String phone, String code, SmsType smsType) {
        String smsContent = buildMessage(code, smsType);
        if (log.isDebugEnabled()) {
            log.debug(String.format("SMS to %s:\n%s", phone, smsContent));
        }

        if (smsProperties.isEnabled()) {
            SmsSendRequest smsSendRequest = new SmsSendRequest(phone, smsContent);
            gateway.sendSms(smsSendRequest);
        }
    }

    private String buildMessage(String value, SmsType smsType) {
        switch (smsType) {
            case REGISTRATION: {
                return String.format(smsProperties.getText().getRegistration(), value);
            }
            case PASSWORD_RESET: {
                return String.format(smsProperties.getText().getResetPassword(), value);
            }
            case NEW_PASSWORD: {
                return String.format(smsProperties.getText().getNewPassword(), value);
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Getter
    @ToString
    @JacksonXmlRootElement(localName = "request")
    public static class SmsSendRequest {
        @JacksonXmlProperty
        private final String operation;
        @JacksonXmlProperty
        private final Message message;

        public SmsSendRequest(String receiver, String smsBody) {
            operation = SEND_SMS_OPERATION;
            message = new Message(receiver, smsBody);
        }
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Message {
        @JacksonXmlProperty(isAttribute = true)
        private int lifetime = 4;
        @JacksonXmlProperty
        private final String recipient;
        @JacksonXmlProperty
        private final String body;
    }
}
