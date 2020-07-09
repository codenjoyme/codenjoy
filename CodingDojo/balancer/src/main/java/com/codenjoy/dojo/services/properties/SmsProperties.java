package com.codenjoy.dojo.services.properties;

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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("sms")
public class SmsProperties {

    private boolean enabled;
    private String staticVerificationCode;
    private String staticPassword;
    private int passwordLength;
    private int codeLength;
    private SmsTextProperties text;
    private GatewayProperties gateway;

    @Getter
    @Setter
    @ConfigurationProperties("text")
    public static class SmsTextProperties {
        private String registration;
        private String resetPassword;
        private String newPassword;
    }

    @Getter
    @Setter
    @ConfigurationProperties("gateway")
    public static class GatewayProperties {
        private String sendEndpoint;
        private String user;
        private String password;
    }

}
