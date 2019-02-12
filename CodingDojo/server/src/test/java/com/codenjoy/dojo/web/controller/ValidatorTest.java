package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.dao.Registration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ValidatorTest {

    private ConfigProperties properties;
    private Registration registration;
    private Validator validator;

    @Before
    public void setUp() {
        validator = new Validator(){{
            ValidatorTest.this.registration = this.registration = mock(Registration.class);
            ValidatorTest.this.properties = this.properties = mock(ConfigProperties.class);
        }};
    }

    @Test
    public void validatePlayerId() {
        shouldError("Player id is invalid: 'null'",
                () -> validator.checkPlayerId(null));

        shouldError("Player id is invalid: ''",
                () -> validator.checkPlayerId(""));

        shouldError("Player id is invalid: 'NuLL'",
                () -> validator.checkPlayerId("NuLL"));

        shouldError("Player id is invalid: 'null'",
                () -> validator.checkPlayerId("null"));

        shouldError("Player id is invalid: 'NULL'",
                () -> validator.checkPlayerId("NULL"));

        shouldError("Player id is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkPlayerId("*F(@DF^@(&@DF(@^"));

        shouldError("Player id is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerId("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        shouldOk(() -> validator.checkPlayerId("1"));

        shouldOk(() -> validator.checkPlayerId("someId"));
    }

    @Test
    public void validateCode() {
        shouldError("Player code is invalid: 'null'",
                () -> validator.checkCode(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkCode(null, CAN_BE_NULL));

        shouldError("Player code is invalid: ''",
                () -> validator.checkCode("", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("", CAN_BE_NULL));

        shouldError("Player code is invalid: 'NuLL'",
                () -> validator.checkCode("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("NuLL", CAN_BE_NULL));

        shouldError("Player code is invalid: 'null'",
                () -> validator.checkCode("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("null", CAN_BE_NULL));

        shouldError("Player code is invalid: 'NULL'",
                () -> validator.checkCode("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("NULL", CAN_BE_NULL));

        shouldError("Player code is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCode("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Player code is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkCode("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("0", CAN_BE_NULL));

        shouldError("",
                () -> validator.checkCode("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldError("Player code is invalid: 'someId'",
                () -> validator.checkCode("someId", CANT_BE_NULL));

        shouldError("Player code is invalid: 'some@email.com'",
                () -> validator.checkCode("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateGameName() {
        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameName(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName(null, CAN_BE_NULL));

        shouldError("Game name is invalid: ''",
                () -> validator.checkGameName("", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NuLL'",
                () -> validator.checkGameName("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("NuLL", CAN_BE_NULL));

        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameName("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("null", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NULL'",
                () -> validator.checkGameName("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("NULL", CAN_BE_NULL));

        shouldError("Game name is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkGameName("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Game name is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkGameName("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Game name is invalid: '-game'",
                () -> validator.checkGameName("-game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game-'",
                () -> validator.checkGameName("game-", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a-game", CANT_BE_NULL));

        shouldError("Game name is invalid: '_game'",
                () -> validator.checkGameName("_game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game_'",
                () -> validator.checkGameName("game_", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a_game", CANT_BE_NULL));

        shouldError("Game name is invalid: '.game'",
                () -> validator.checkGameName(".game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game.'",
                () -> validator.checkGameName("game.", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a.game", CANT_BE_NULL));

        shouldError("Game name is invalid: '1'",
                () -> validator.checkGameName("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a1", CANT_BE_NULL));

        shouldError("Game name is invalid: '0'",
                () -> validator.checkGameName("0", CAN_BE_NULL));

        shouldError("Game name is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkGameName("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("someGame", CANT_BE_NULL));

        shouldError("Game name is invalid: 'some@email.com'",
                () -> validator.checkGameName("some@email.com", CANT_BE_NULL));
    }

    private void shouldOk(Runnable toRun) {
        shouldError("", toRun);
    }

    private void shouldError(String expectedException, Runnable toRun) {
        try {
            if (toRun != null) {
                toRun.run();
            }
            if (StringUtils.isNotEmpty(expectedException)) {
                fail();
            }
        } catch (Exception e) {
            assertEquals(expectedException, e.getMessage());
        }
    }

}