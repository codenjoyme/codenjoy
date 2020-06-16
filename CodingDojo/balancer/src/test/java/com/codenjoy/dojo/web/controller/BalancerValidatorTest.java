package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.properties.Messages;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BalancerValidatorTest {

    private Messages messages;
    private BalancerValidator validator;

    @Before
    public void setUp() {
        validator = new BalancerValidator() {{
            BalancerValidatorTest.this.messages = this.messages = mock(Messages.class);
            this.players = mock(Players.class);
            this.properties = mock(ConfigProperties.class);
            this.passwordEncoder = new BCryptPasswordEncoder();
        }};
    }

    private void shouldOk(Runnable toRun) {
        shouldError("", toRun);
    }

    private void shouldError(String expected, Runnable toRun) {
        try {
            if (toRun != null) {
                toRun.run();
            }
            if (StringUtils.isNotEmpty(expected)) {
                fail();
            }
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testCheckName() {
        when(messages.getInvalidStringFormat()).thenReturn(
                "Використано неприпустимі символи у %s (допустимі '%s') та/або перевищення " +
                        "максимальної довжини в %s символв: '%s'");

        when(messages.getInvalidString()).thenReturn(
                "Очікується не порожній рядок %s: '%s'");

        String expected = "Використано неприпустимі символи у field (допустимі " +
                "'[a-zA-Zа-яА-Я'іІєЄґҐїЇ]') та/або перевищення " +
                "максимальної довжини в 50 символв: ";

        shouldError("Очікується не порожній рядок field: ''",
                () -> validator.checkName("field", ""));

        shouldError("Очікується не порожній рядок field: 'null'",
                () -> validator.checkName("field", null));

        shouldError(expected + "'==+Oleksandr+=='",
                () -> validator.checkName("field", "==+Oleksandr+=="));

        shouldError(expected + "'O l e k s a n d r'",
                () -> validator.checkName("field", "O l e k s a n d r"));

        shouldError(expected + "'!!!'",
                () -> validator.checkName("field", "!!!"));

        shouldError(expected + "'ββ ζζ'",
                () -> validator.checkName("field", "ββ ζζ"));

        shouldError(expected + "'123456789012345678901234567890123456789012345678901'",
                () -> validator.checkName("field", "123456789012345678901234567890123456789012345678901"));

        shouldOk(() -> validator.checkName("field", "Oleksandr"));
    }

}