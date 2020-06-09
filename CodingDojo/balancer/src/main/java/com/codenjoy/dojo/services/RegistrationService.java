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

import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.properties.SmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.codenjoy.dojo.services.entity.Player.APPROVED;
import static com.codenjoy.dojo.services.entity.Player.NOT_APPROVED;

@Slf4j
@Service
public class RegistrationService {

    private final static int CODE_LENGTH = 6;

    public enum VerificationType {REGISTRATION, PASSWORD_RESET}

    @Autowired private SmsService sms;
    @Autowired private Players players;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SmsProperties smsProperties;

    public String generateVerificationCode() {
        if (smsProperties.isEnabled()) {
            return RandomStringUtils.randomNumeric(CODE_LENGTH);
        }
        return smsProperties.getStaticVerificationCode();
    }

    public String generateId() {
        return Hash.getRandomId();
    }

    public ServerLocation confirmRegistration(String phone, String code) {
        Player player = getByPhone(phone);

        if (player.getApproved() == APPROVED) {
            throw new IllegalArgumentException("User already confirmed");
        }

        if (validateCode(code, VerificationType.REGISTRATION, player)) {
            players.approveByPhone(phone);
            players.updateVerificationCode(phone, null, null);
            return new ServerLocation(player);
        }

        throw new IllegalArgumentException("Invalid verification code");
    }

    public void resendConfirmRegistrationCode(String phone) {
        Player player = getByPhone(phone);
        if (player.getApproved() == APPROVED) {
            throw new IllegalArgumentException("User already confirmed");
        }
        String verificationCode = generateVerificationCode();
        players.updateVerificationCode(phone, verificationCode, VerificationType.REGISTRATION.name());
        sms.sendSmsTo(phone, verificationCode, SmsService.SmsType.REGISTRATION);
    }

    public void resendResetPasswordCode(String phone) {
        Player player = players.getByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (player.getApproved() == NOT_APPROVED) {
            throw new IllegalArgumentException("User is not active");
        }

        String verificationCode = generateVerificationCode();
        players.updateVerificationCode(phone, verificationCode, VerificationType.PASSWORD_RESET.name());
        sms.sendSmsTo(phone, verificationCode, SmsService.SmsType.PASSWORD_RESET);
    }

    public boolean validateCodeResetPassword(String phone, String code) {
        Player player = getByPhone(phone);

        if (player.getApproved() == NOT_APPROVED) {
            throw new IllegalArgumentException("User is not active");
        }

        if (validateCode(code, VerificationType.PASSWORD_RESET, player)) {
            players.updateVerificationCode(phone, null, null);
            return true;
        }

        return false;
    }

    public void resetPassword(String phone) {
        Player player = getByPhone(phone);

        String generated = generatePassword();
        // это делает фронтенд, но у нас тут чистый пароль
        String md5 = DigestUtils.md5Hex(generated);
        // еще раз захешируем
        String hashed = passwordEncoder.encode(md5);

        player.setPassword(hashed);
        // и подсчитаем code(md5(bcrypt(password)))
        player.setCode(Hash.getCode(player.getId(), hashed));

        players.update(player);
        sms.sendSmsTo(phone, generated, SmsService.SmsType.NEW_PASSWORD);
    }

    private boolean validateCode(String smsCode, VerificationType codeType, Player player) {
        return (!StringUtils.isEmpty(smsCode) && smsCode.equals(player.getVerificationCode()))
                && (codeType != null && codeType.name().equals(player.getVerificationType()));
    }

    private Player getByPhone(String phone) {
        return players.getByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String generatePassword() {
        if (smsProperties.isEnabled()) {
            return RandomStringUtils.randomAlphabetic(8);
        }

        return smsProperties.getStaticPassword();
    }
}

