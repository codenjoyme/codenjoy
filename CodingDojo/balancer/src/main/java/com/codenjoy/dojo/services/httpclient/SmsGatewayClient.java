package com.codenjoy.dojo.services.httpclient;

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

import com.codenjoy.dojo.services.SmsService;
import com.codenjoy.dojo.services.properties.SmsProperties;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sms-gateway",
        url = "${sms.gateway.send-endpoint}",
        configuration = SmsGatewayClient.SmsGatewayClientConfig.class)
public interface SmsGatewayClient {

    @PostMapping(value = "", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    ResponseEntity<String> sendSms(@RequestBody SmsService.SmsSendRequest smsSendRequest);

    class SmsGatewayClientConfig {

        @Autowired private SmsProperties smsProperties;

        @Bean
        public RequestInterceptor basicAuthRequestInterceptor() {
            return new BasicAuthRequestInterceptor(smsProperties.getGateway().getUser(),
                    smsProperties.getGateway().getPassword());
        }
    }
}
