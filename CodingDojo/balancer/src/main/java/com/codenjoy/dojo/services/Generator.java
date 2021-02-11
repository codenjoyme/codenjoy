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

import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.properties.SmsProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Generator {

    @Autowired
    private SmsProperties sms;

    public String id() {
        return Hash.getRandomId();
    }

    public String verificationCode() {
        if (sms.isEnabled()) {
            return RandomStringUtils.randomNumeric(sms.getCodeLength());
        }
        return sms.getStaticVerificationCode();
    }

    public String password() {
        if (sms.isEnabled()) {
            return RandomStringUtils.randomAlphabetic(sms.getPasswordLength());
        }

        return sms.getStaticPassword();
    }
}
