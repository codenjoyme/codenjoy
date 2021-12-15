package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.utils.smart.SmartAssert;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static com.codenjoy.dojo.web.controller.BoardController.queryToMap;
import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO try @SpringBootTest
public class ValidatorTest {

    private ConfigProperties properties;
    private Registration registration;
    private Validator validator;
    private GameService gameService;
    private PlayerService playerService;

    @Before
    public void setup() {
        validator = new Validator() {{
            ValidatorTest.this.registration = this.registration = mock(Registration.class);
            ValidatorTest.this.properties = this.properties = mock(ConfigProperties.class);
            ValidatorTest.this.gameService = this.gameService = mock(GameService.class);
            ValidatorTest.this.playerService = this.playerService = mock(PlayerService.class);
        }};
    }

    @After
    public void after() {
        SmartAssert.checkResult();

    }

    @Test
    public void validateCheckPlayerId_only() {
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
    public void validateCheckEmail() {
        shouldError("Player email is invalid: 'null'",
                () -> validator.checkEmail(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail(null, CAN_BE_NULL));

        shouldError("Player email is invalid: 'null'",
                () -> validator.checkEmail("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail("null", CAN_BE_NULL));

        shouldError("Player email is invalid: ''",
                () -> validator.checkEmail("", CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail("", CAN_BE_NULL));

        shouldError("Player email is invalid: 'NuLL'",
                () -> validator.checkEmail("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail("NuLL", CAN_BE_NULL));

        shouldError("Player email is invalid: 'NULL'",
                () -> validator.checkEmail("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail("NULL", CAN_BE_NULL));

        shouldError("Player email is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkEmail("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Player email is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkEmail("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldOk(() -> validator.checkEmail("qwe@asd.com", CANT_BE_NULL));

        shouldError("Player email is invalid: 'someId'",
                () -> validator.checkEmail("someId", CANT_BE_NULL));
    }

    @Test
    public void validateIsEmail() {
        
        assertEquals(false, validator.isEmail(null, CANT_BE_NULL));

        assertEquals(true, validator.isEmail(null, CAN_BE_NULL));

        assertEquals(false, validator.isEmail("null", CANT_BE_NULL));

        assertEquals(true, validator.isEmail("null", CAN_BE_NULL));

        assertEquals(false, validator.isEmail("", CANT_BE_NULL));

        assertEquals(true, validator.isEmail("", CAN_BE_NULL));

        assertEquals(false, validator.isEmail("NuLL", CANT_BE_NULL));

        assertEquals(true, validator.isEmail("NuLL", CAN_BE_NULL));

        assertEquals(false, validator.isEmail("NULL", CANT_BE_NULL));

        assertEquals(true, validator.isEmail("NULL", CAN_BE_NULL));

        assertEquals(false, validator.isEmail("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        assertEquals(false, validator.isEmail("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        assertEquals(true, validator.isEmail("qwe@asd.com", CANT_BE_NULL));

        assertEquals(false, validator.isEmail("someId", CANT_BE_NULL));
    }

    @Test
    public void validateCheckCode() {
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

        shouldOk(() -> validator.checkCode("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldError("Player code is invalid: 'someId'",
                () -> validator.checkCode("someId", CANT_BE_NULL));

        shouldError("Player code is invalid: 'some@email.com'",
                () -> validator.checkCode("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateIsGameName() {
        assertEquals(false, validator.isGameName(null, CANT_BE_NULL));

        assertEquals(true, validator.isGameName(null, CAN_BE_NULL));

        assertEquals(false, validator.isGameName("", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("", CAN_BE_NULL));

        assertEquals(false, validator.isGameName("NuLL", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("NuLL", CAN_BE_NULL));

        assertEquals(false, validator.isGameName("null", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("null", CAN_BE_NULL));

        assertEquals(false, validator.isGameName("NULL", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("NULL", CAN_BE_NULL));

        assertEquals(false, validator.isGameName("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("-game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("game-", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("a-game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("_game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("game_", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("a_game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName(".game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("game.", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("a.game", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("1", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("a1", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("a1", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("0", CAN_BE_NULL));

        assertEquals(false, validator.isGameName("434589345613405760956134056340596345903465", CANT_BE_NULL));

        assertEquals(true, validator.isGameName("someGame", CANT_BE_NULL));

        assertEquals(false, validator.isGameName("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateCheckGameName() {
        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGame(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkGame(null, CAN_BE_NULL));

        shouldError("Game name is invalid: ''",
                () -> validator.checkGame("", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NuLL'",
                () -> validator.checkGame("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("NuLL", CAN_BE_NULL));

        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGame("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("null", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NULL'",
                () -> validator.checkGame("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("NULL", CAN_BE_NULL));

        shouldError("Game name is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkGame("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Game name is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkGame("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Game name is invalid: '-game'",
                () -> validator.checkGame("-game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game-'",
                () -> validator.checkGame("game-", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("a-game", CANT_BE_NULL));

        shouldError("Game name is invalid: '_game'",
                () -> validator.checkGame("_game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game_'",
                () -> validator.checkGame("game_", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("a_game", CANT_BE_NULL));

        shouldError("Game name is invalid: '.game'",
                () -> validator.checkGame(".game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game.'",
                () -> validator.checkGame("game.", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("a.game", CANT_BE_NULL));

        shouldError("Game name is invalid: '1'",
                () -> validator.checkGame("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("a1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("a1", CANT_BE_NULL));

        shouldError("Game name is invalid: '0'",
                () -> validator.checkGame("0", CAN_BE_NULL));

        shouldError("Game name is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkGame("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldOk(() -> validator.checkGame("someGame", CANT_BE_NULL));

        shouldError("Game name is invalid: 'some@email.com'",
                () -> validator.checkGame("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateCheckRoomName() {
        shouldError("Room name is invalid: 'null'",
                () -> validator.checkRoom(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom(null, CAN_BE_NULL));

        shouldError("Room name is invalid: ''",
                () -> validator.checkRoom("", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("", CAN_BE_NULL));

        shouldError("Room name is invalid: 'NuLL'",
                () -> validator.checkRoom("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("NuLL", CAN_BE_NULL));

        shouldError("Room name is invalid: 'null'",
                () -> validator.checkRoom("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("null", CAN_BE_NULL));

        shouldError("Room name is invalid: 'NULL'",
                () -> validator.checkRoom("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("NULL", CAN_BE_NULL));

        shouldError("Room name is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkRoom("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Room name is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkRoom("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Room name is invalid: '-game'",
                () -> validator.checkRoom("-game", CANT_BE_NULL));

        shouldError("Room name is invalid: 'game-'",
                () -> validator.checkRoom("game-", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("a-game", CANT_BE_NULL));

        shouldError("Room name is invalid: '_game'",
                () -> validator.checkRoom("_game", CANT_BE_NULL));

        shouldError("Room name is invalid: 'game_'",
                () -> validator.checkRoom("game_", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("a_game", CANT_BE_NULL));

        shouldError("Room name is invalid: '.game'",
                () -> validator.checkRoom(".game", CANT_BE_NULL));

        shouldError("Room name is invalid: 'game.'",
                () -> validator.checkRoom("game.", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("a.game", CANT_BE_NULL));

        shouldError("Room name is invalid: '1'",
                () -> validator.checkRoom("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("a1", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("a1", CANT_BE_NULL));

        shouldError("Room name is invalid: '0'",
                () -> validator.checkRoom("0", CAN_BE_NULL));

        shouldError("Room name is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkRoom("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldOk(() -> validator.checkRoom("someGame", CANT_BE_NULL));

        shouldError("Room name is invalid: 'some@email.com'",
                () -> validator.checkRoom("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateCheckMD5() {
        shouldError("Hash is invalid: 'null'",
                () -> validator.checkMD5(null));

        shouldError("Hash is invalid: ''",
                () -> validator.checkMD5(""));

        shouldError("Hash is invalid: 'NuLL'",
                () -> validator.checkMD5("NuLL"));

        shouldError("Hash is invalid: 'null'",
                () -> validator.checkMD5("null"));

        shouldError("Hash is invalid: 'NULL'",
                () -> validator.checkMD5("NULL"));

        shouldError("Hash is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkMD5("*F(@DF^@(&@DF(@^"));

        shouldError("Hash is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkMD5("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        shouldError("Hash is invalid: '3-13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3-13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '3_13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3_13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '3.13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3.13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '1'",
                () -> validator.checkMD5("1"));

        shouldError("Hash is invalid: '0'",
                () -> validator.checkMD5("0"));

        shouldError("Hash is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkMD5("434589345613405760956134056340596345903465"));

        shouldOk(() -> validator.checkMD5("313dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: 'some@email.com'",
                () -> validator.checkMD5("some@email.com"));
    }

    @Test
    public void validateCheckCustomQueryParameterName() {
        shouldError("Custom query parameter name is invalid: 'null'",
                () -> validator.checkCustomQueryParameterName(null));

        shouldError("Custom query parameter name is invalid: ''",
                () -> validator.checkCustomQueryParameterName(""));

        shouldError("Custom query parameter name is invalid: 'NuLL'",
                () -> validator.checkCustomQueryParameterName("NuLL"));

        shouldError("Custom query parameter name is invalid: 'null'",
                () -> validator.checkCustomQueryParameterName("null"));

        shouldError("Custom query parameter name is invalid: 'NULL'",
                () -> validator.checkCustomQueryParameterName("NULL"));

        shouldError("Custom query parameter name is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCustomQueryParameterName("*F(@DF^@(&@DF(@^"));

        shouldError("Custom query parameter name is invalid: 'toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkCustomQueryParameterName("toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        shouldError("Custom query parameter name is invalid: '1'",
                () -> validator.checkCustomQueryParameterName("1"));

        shouldError("Custom query parameter name is invalid: '0'",
                () -> validator.checkCustomQueryParameterName("0"));

        shouldOk(() -> validator.checkCustomQueryParameterName("name"));

        shouldOk(() -> validator.checkCustomQueryParameterName("Name"));

        shouldOk(() -> validator.checkCustomQueryParameterName("NAME"));

        shouldOk(() -> validator.checkCustomQueryParameterName("name12"));

        shouldOk(() -> validator.checkCustomQueryParameterName("na12me"));

        shouldError("Custom query parameter name is invalid: '12name'",
                () -> validator.checkCustomQueryParameterName("12name"));

        shouldError("Custom query parameter name is invalid: '.name'",
                () -> validator.checkCustomQueryParameterName(".name"));

        shouldError("Custom query parameter name is invalid: '_name'",
                () -> validator.checkCustomQueryParameterName("_name"));

        shouldError("Custom query parameter name is invalid: '-name'",
                () -> validator.checkCustomQueryParameterName("-name"));

        shouldError("Custom query parameter name is invalid: 'name-'",
                () -> validator.checkCustomQueryParameterName("name-"));

        shouldError("Custom query parameter name is invalid: 'name_'",
                () -> validator.checkCustomQueryParameterName("name_"));

        shouldError("Custom query parameter name is invalid: 'name.'",
                () -> validator.checkCustomQueryParameterName("name."));

        shouldError("Custom query parameter name is invalid: 'n-ame'",
                () -> validator.checkCustomQueryParameterName("n-ame"));

        shouldError("Custom query parameter name is invalid: 'n_ame'",
                () -> validator.checkCustomQueryParameterName("n_ame"));

        shouldError("Custom query parameter name is invalid: 'n.ame'",
                () -> validator.checkCustomQueryParameterName("n.ame"));

        shouldError("Custom query parameter name is invalid: 'some@email.com'",
                () -> validator.checkCustomQueryParameterName("some@email.com"));
    }

    @Test
    public void validateCheckCustomQueryParameterValue() {
        shouldError("Custom query parameter 'key' value is invalid: 'null'",
                () -> validator.checkCustomQueryParameterValue("key", null));

        shouldError("Custom query parameter 'key' value is invalid: ''",
                () -> validator.checkCustomQueryParameterValue("key", ""));

        shouldError("Custom query parameter 'key' value is invalid: 'NuLL'",
                () -> validator.checkCustomQueryParameterValue("key", "NuLL"));

        shouldError("Custom query parameter 'key' value is invalid: 'null'",
                () -> validator.checkCustomQueryParameterValue("key", "null"));

        shouldError("Custom query parameter 'key' value is invalid: 'NULL'",
                () -> validator.checkCustomQueryParameterValue("key", "NULL"));

        shouldError("Custom query parameter 'key' value is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCustomQueryParameterValue("key", "*F(@DF^@(&@DF(@^"));

        shouldError("Custom query parameter 'key' value is invalid: 'toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkCustomQueryParameterValue("key", "toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "1"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "0"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "Name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "NAME"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "name12"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "na12me"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "12name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", ".name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "_name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "-name"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "name-"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "name_"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "name."));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "n-ame"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "n_ame"));

        shouldOk(() -> validator.checkCustomQueryParameterValue("key", "n.ame"));

        shouldError("Custom query parameter 'key' value is invalid: 'some@email.com'",
                () -> validator.checkCustomQueryParameterValue("key", "some@email.com"));
    }


    @Test
    public void validateIsCustomQueryParameterName() {
        assertEquals(false, validator.isCustomQueryParameterName(null));

        assertEquals(false, validator.isCustomQueryParameterName(""));

        assertEquals(false, validator.isCustomQueryParameterName("NuLL"));

        assertEquals(false, validator.isCustomQueryParameterName("null"));

        assertEquals(false, validator.isCustomQueryParameterName("NULL"));

        assertEquals(false, validator.isCustomQueryParameterName("*F(@DF^@(&@DF(@^"));

        assertEquals(false, validator.isCustomQueryParameterName("toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertEquals(false, validator.isCustomQueryParameterName("1"));

        assertEquals(false, validator.isCustomQueryParameterName("0"));

        assertEquals(true, validator.isCustomQueryParameterName("name"));

        assertEquals(true, validator.isCustomQueryParameterName("Name"));

        assertEquals(true, validator.isCustomQueryParameterName("NAME"));

        assertEquals(true, validator.isCustomQueryParameterName("name12"));

        assertEquals(true, validator.isCustomQueryParameterName("na12me"));

        assertEquals(false, validator.isCustomQueryParameterName("12name"));

        assertEquals(false, validator.isCustomQueryParameterName(".name"));

        assertEquals(false, validator.isCustomQueryParameterName("_name"));

        assertEquals(false, validator.isCustomQueryParameterName("-name"));

        assertEquals(false, validator.isCustomQueryParameterName("name-"));

        assertEquals(false, validator.isCustomQueryParameterName("name_"));

        assertEquals(false, validator.isCustomQueryParameterName("name."));

        assertEquals(false, validator.isCustomQueryParameterName("n-ame"));

        assertEquals(false, validator.isCustomQueryParameterName("n_ame"));

        assertEquals(false, validator.isCustomQueryParameterName("n.ame"));

        assertEquals(false, validator.isCustomQueryParameterName("some@email.com"));
    }

    @Test
    public void validateIsCustomQueryParameterValue() {
        assertEquals(false, validator.isCustomQueryParameterValue(null));

        assertEquals(false, validator.isCustomQueryParameterValue(""));

        assertEquals(false, validator.isCustomQueryParameterValue("NuLL"));

        assertEquals(false, validator.isCustomQueryParameterValue("null"));

        assertEquals(false, validator.isCustomQueryParameterValue("NULL"));

        assertEquals(false, validator.isCustomQueryParameterValue("*F(@DF^@(&@DF(@^"));

        assertEquals(false, validator.isCustomQueryParameterValue("toolargeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertEquals(true, validator.isCustomQueryParameterValue("1"));

        assertEquals(true, validator.isCustomQueryParameterValue("0"));

        assertEquals(true, validator.isCustomQueryParameterValue("name"));

        assertEquals(true, validator.isCustomQueryParameterValue("Name"));

        assertEquals(true, validator.isCustomQueryParameterValue("NAME"));

        assertEquals(true, validator.isCustomQueryParameterValue("name12"));

        assertEquals(true, validator.isCustomQueryParameterValue("na12me"));

        assertEquals(true, validator.isCustomQueryParameterValue("12name"));

        assertEquals(true, validator.isCustomQueryParameterValue(".name"));

        assertEquals(true, validator.isCustomQueryParameterValue("_name"));

        assertEquals(true, validator.isCustomQueryParameterValue("-name"));

        assertEquals(true, validator.isCustomQueryParameterValue("name-"));

        assertEquals(true, validator.isCustomQueryParameterValue("name_"));

        assertEquals(true, validator.isCustomQueryParameterValue("name."));

        assertEquals(true, validator.isCustomQueryParameterValue("n-ame"));

        assertEquals(true, validator.isCustomQueryParameterValue("n_ame"));

        assertEquals(true, validator.isCustomQueryParameterValue("n.ame"));

        assertEquals(false, validator.isCustomQueryParameterValue("some@email.com"));
    }

    @Test
    public void validateIsMD5() {
        assertEquals(false, validator.isMD5(null));

        assertEquals(false, validator.isMD5(""));

        assertEquals(false, validator.isMD5("NuLL"));

        assertEquals(false, validator.isMD5("null"));

        assertEquals(false, validator.isMD5("NULL"));

        assertEquals(false, validator.isMD5("*F(@DF^@(&@DF(@^"));

        assertEquals(false, validator.isMD5("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertEquals(false, validator.isMD5("3-13dc7cb57f02b9c7c066b9e34b6fe72"));

        assertEquals(false, validator.isMD5("3_13dc7cb57f02b9c7c066b9e34b6fe72"));

        assertEquals(false, validator.isMD5("3.13dc7cb57f02b9c7c066b9e34b6fe72"));

        assertEquals(false, validator.isMD5("1"));

        assertEquals(false, validator.isMD5("0"));

        assertEquals(false, validator.isMD5("434589345613405760956134056340596345903465"));

        assertEquals(true, validator.isMD5("313dc7cb57f02b9c7c066b9e34b6fe72"));

        assertEquals(false, validator.isMD5("some@email.com"));
    }

    @Test
    public void validateCheckCommand() {
        shouldError("Command is invalid: 'null'",
                () -> validator.checkCommand(null));

        shouldError("Command is invalid: ''",
                () -> validator.checkCommand(""));

        shouldError("Command is invalid: 'NuLL'",
                () -> validator.checkCommand("NuLL"));

        shouldError("Command is invalid: 'null'",
                () -> validator.checkCommand("null"));

        shouldError("Command is invalid: 'NULL'",
                () -> validator.checkCommand("NULL"));

        shouldError("Command is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCommand("*F(@DF^@(&@DF(@^"));

        shouldError("Command is invalid: '3-13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3-13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '3_13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3_13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '3.13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3.13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '1'",
                () -> validator.checkCommand("1"));

        shouldError("Command is invalid: '0'",
                () -> validator.checkCommand("0"));

        shouldError("Command is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkCommand("434589345613405760956134056340596345903465"));

        shouldOk(() -> validator.checkCommand("act(1,2,3),Left,messAGE('SOME TEXT'),RIGHT,ACT(),down,uP,act(1, 2 , 3)  ,DOWN"));

        shouldOk(() -> validator.checkCommand("act()"));

        shouldOk(() -> validator.checkCommand("DOWN"));

        shouldOk(() -> validator.checkCommand("leFT"));

        shouldOk(() -> validator.checkCommand("right,up"));

        shouldOk(() -> validator.checkCommand("left()"));

        shouldOk(() -> validator.checkCommand("act()()"));

        shouldOk(() -> validator.checkCommand("act(1,       2)"));

        shouldOk(() -> validator.checkCommand("act"));

        shouldOk(() -> validator.checkCommand("act(1, 34"));

        // TODO вот тут как-то не совсем верно
        shouldOk(() -> validator.checkCommand("right?up"));
        shouldOk(() -> validator.checkCommand("-right,up"));
        shouldOk(() -> validator.checkCommand("&^@#%&^right@#$&*up@$"));

        shouldError("Command is invalid: 'qwe'",
                () -> validator.checkCommand("qwe"));

        shouldError("Command is invalid: 'qwe,asd'",
                () -> validator.checkCommand("qwe,asd"));

        shouldError("Command is invalid: 'qwe(1,3)'",
                () -> validator.checkCommand("qwe(1,3)"));

        shouldOk(() -> validator.checkCommand("message('кириллица')"));

        shouldOk(() -> validator.checkCommand("message('latin')"));

        shouldOk(() -> validator.checkCommand("message(''')"));

        shouldOk(() -> validator.checkCommand("message(''''''''')"));

        shouldError("Command is invalid: 'messAGE('TOO LARGE'),message('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa')'",
                () -> validator.checkCommand("messAGE('TOO LARGE'),message('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa')"));

        shouldError("Command is invalid: 'some@email.com'",
                () -> validator.checkCommand("some@email.com"));

        shouldOk(() -> validator.checkCommand("message('some@email.com')"));
    }

    @Test
    public void validateCheckPlayerCode() {
        when(registration.checkUser(anyString(), anyString())).thenAnswer(inv -> inv.getArgument(0));

        shouldError("Player id is invalid: 'email@gmail.com'",
                () -> validator.checkPlayerCode("email@gmail.com", "12345678901234567890"));

        shouldOk(() -> validator.checkPlayerCode("validPlayerId", "12345678901234567890"));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa'",
                () -> validator.checkPlayerCode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa", "12345678901234567890"));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerCode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "12345678901234567890"));

        shouldError("Player code is invalid: '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000'",
                () -> validator.checkPlayerCode("codePlayerId", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));

        shouldError("Player id is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkPlayerCode("email#&*^#gmail%#&^*com", "12345678901234567890"));

        shouldError("Player code is invalid: '12dehgfsgfsdlfidfj90'",
                () -> validator.checkPlayerCode("validPlayerId", "12dehgfsgfsdlfidfj90"));

        shouldError("Player id is invalid: 'null'",
                () -> validator.checkPlayerCode(null, "12345678901234567890"));

        shouldError("Player code is invalid: 'null'",
                () -> validator.checkPlayerCode("validPlayerId", null));
    }

    @Test
    public void validateCheckPlayerId() {
        shouldOk(() -> validator.checkPlayerId("validPlayerId", CANT_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("valid-player-id", CANT_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("codePlayerId", CANT_BE_NULL));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa'",
                () -> validator.checkPlayerId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa", CANT_BE_NULL));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Player id is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkPlayerId("email#&*^#gmail%#&^*com", CANT_BE_NULL));

        shouldError("Player id is invalid: 'null'",
                () -> validator.checkPlayerId(null, CANT_BE_NULL));

        shouldError("Player id is invalid: 'NuLL'",
                () -> validator.checkPlayerId("NuLL", CANT_BE_NULL));

        shouldError("Player id is invalid: ''",
                () -> validator.checkPlayerId("", CANT_BE_NULL));

        shouldError("Player id is invalid: 'null'",
                () -> validator.checkPlayerId(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkPlayerId(null, CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("null", CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("", CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("nULL", CAN_BE_NULL));
    }

    @Test
    public void validateIsReadablePlayerName() {
        assertEquals(true, validator.isReadableName("Стивен Пупкин"));

        assertEquals(true, validator.isReadableName("Oleksandr Baglay"));

        assertEquals(true, validator.isReadableName("Stiven Pupkin"));

        assertEquals(true, validator.isReadableName("стивен пупкин"));

        assertEquals(true, validator.isReadableName("stiven pupkin"));

        assertEquals(true, validator.isReadableName("ABCDEFGHIJKLMNOPQRSTUVQXYZ abcdefghijklmnopqrstuvqxyz"));

        assertEquals(true, validator.isReadableName("abcdefghijklmnopqrstuvqxyz ABCDEFGHIJKLMNOPQRSTUVQXYZ"));

        assertEquals(true, validator.isReadableName("абвгдеёжзийклмо НПРСТУФХЧЦЬЫЪЭЮЯ"));

        assertEquals(true, validator.isReadableName("нпрстуфхчцьыъэюя АБВГДЕЁЖЗИЙКЛМО"));

        assertEquals(true, validator.isReadableName("АБВГДЕЁЖЗИЙКЛМО нпрстуфхчцьыъэюя"));

        assertEquals(true, validator.isReadableName("НПРСТУФХЧЦЬЫЪЭЮЯ абвгдеёжзийклмо"));

        assertEquals(true, validator.isReadableName("ҐґІіІіЄє ҐґІіІіЄє"));

        assertEquals(false, validator.isReadableName("Стивен"));

        assertEquals(false, validator.isReadableName("Я Д'Артаньян"));

        assertEquals(false, validator.isReadableName("Дефис-нельзя"));

        assertEquals(false, validator.isReadableName("Двапробела  нельзя"));

        assertEquals(false, validator.isReadableName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaa"));

        assertEquals(false, validator.isReadableName("email#&*^#gmail%#&^*com"));

        assertEquals(false, validator.isReadableName(null));

        assertEquals(false, validator.isReadableName("NuLL"));

        assertEquals(false, validator.isReadableName(""));

        assertEquals(false, validator.isReadableName(null));
    }

    @Test
    public void validateIsNickName() {
        assertEquals(true, validator.isNickName("Стивен Пупкин"));

        assertEquals(true, validator.isNickName("Oleksandr Baglay"));

        assertEquals(true, validator.isNickName("Stiven Pupkin"));

        assertEquals(true, validator.isNickName("FuTuRamA"));

        assertEquals(true, validator.isNickName("BLIvl evLVe cyuw7 82fx"));

        assertEquals(true, validator.isNickName("123 444 56 7890 2231"));

        assertEquals(true, validator.isNickName("стивен пупкин"));

        assertEquals(true, validator.isNickName("stiven pupkin"));

        assertEquals(true, validator.isNickName("ABCDEFGHIJKLMNOP abcdefghijklmnop"));

        assertEquals(true, validator.isNickName("QRSTUVQXYZ qrstuvqxyz"));

        assertEquals(true, validator.isNickName("qrstuvqxyz QRSTUVQXYZ"));

        assertEquals(true, validator.isNickName("abcdefghijklmnop ABCDEFGHIJKLMNOP"));

        assertEquals(true, validator.isNickName("абвгдеёжзийклмо НПРСТУФХЧЦЬЫЪЭЮЯ"));

        assertEquals(true, validator.isNickName("нпрстуфхчцьыъэюя АБВГДЕЁЖЗИЙКЛМО"));

        assertEquals(true, validator.isNickName("АБВГДЕЁЖЗИЙКЛМО нпрстуфхчцьыъэюя"));

        assertEquals(true, validator.isNickName("НПРСТУФХЧЦЬЫЪЭЮЯ абвгдеёжзийклмо"));

        assertEquals(true, validator.isNickName("ҐґІіІіЄє ҐґІіІіЄє"));

        assertEquals(true, validator.isNickName("Стивен"));

        assertEquals(false, validator.isNickName("Я Д'Артаньян"));

        assertEquals(false, validator.isNickName("Дефис-нельзя"));

        assertEquals(true, validator.isNickName("Двапробела  нельзя"));

        assertEquals(false, validator.isNickName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaa"));

        assertEquals(false, validator.isNickName("email#&*^#gmail%#&^*com"));

        assertEquals(false, validator.isNickName(null));

        assertEquals(false, validator.isNickName("NuLL"));

        assertEquals(false, validator.isNickName(""));

        assertEquals(false, validator.isNickName(null));
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
                fail("Expected exception");
            }
        } catch (Exception exception) {
            assertEquals(expected, exception.getMessage());
        }
    }

    @Test
    public void validateCheckNotEmpty() {
        shouldError("Parameter name is empty: 'Null'",
                () -> validator.checkNotEmpty("name", "Null"));

        shouldError("Parameter name is empty: 'null'",
                () -> validator.checkNotEmpty("name", null));

        shouldError("Parameter name is empty: 'NULL'",
                () -> validator.checkNotEmpty("name", "NULL"));

        shouldError("Parameter name is empty: 'null'",
                () -> validator.checkNotEmpty("name", "null"));

        shouldError("Parameter name is empty: ''",
                () -> validator.checkNotEmpty("name", ""));

        shouldOk(() -> validator.checkNotEmpty("name", "not-empty"));
    }

    @Test
    public void validateCheckGameType() {
        when(gameService.exists(anyString())).thenReturn(false);

        // empty string
        shouldError("Game name is invalid: 'Null'",
                () -> validator.checkGameType("Null"));

        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameType(null));

        shouldError("Game name is invalid: 'NULL'",
                () -> validator.checkGameType("NULL"));

        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameType("null"));

        shouldError("Game name is invalid: ''",
                () -> validator.checkGameType(""));

        shouldError("Game not found: not-empty",
                () -> validator.checkGameType("not-empty"));

        // other cases
        when(gameService.exists("valid-game")).thenReturn(true);
        when(gameService.exists("bad-game")).thenReturn(false);

        shouldOk(() -> validator.checkGameType("valid-game"));

        shouldError("Game not found: bad-game",
                () -> validator.checkGameType("bad-game"));
    }

    @Test
    public void validateCheckPlayerInRoom() {
        givenPlayer("validPlayerId", "validRoomName");
        givenPlayer("otherPlayer", "otherRoomName");

        shouldOk(() -> validator.checkPlayerInRoom("validPlayerId", "validRoomName"));

        shouldError("Player id is invalid: '$bad$'",
                () -> validator.checkPlayerInRoom("$bad$", "validRoomName"));

        shouldError("Room name is invalid: '$bad$'",
                () -> validator.checkPlayerInRoom("validPlayerId", "$bad$"));

        shouldError("Player 'otherPlayer' is not in room 'validRoomName'",
                () -> validator.checkPlayerInRoom("otherPlayer", "validRoomName"));
    }

    private void givenPlayer(String id, String room) {
        when(playerService.get(id))
                .thenReturn(new Player(id){{
                    setRoom(room);
                }});
    }

    @Test
    public void validateCheckCustomQueryParameters() {
        shouldError("Custom query is invalid: [parameter name is invalid: 'q$we']",
                () -> validator.checkCustomQueryParameters(queryToMap("q$we=1")));

        shouldError("Custom query is invalid: [parameter 'qwe' value is invalid: '']",
                () -> validator.checkCustomQueryParameters(queryToMap("qwe=")));

        shouldError("Custom query is invalid: [parameter 'qwe' value is invalid: '']",
                () -> validator.checkCustomQueryParameters(queryToMap("qwe=&&")));

        // TODO точно так ок?
        shouldOk(() -> validator.checkCustomQueryParameters(queryToMap("=")));

        shouldOk(() -> validator.checkCustomQueryParameters(queryToMap(null)));

        shouldError("Custom query is invalid: [" +
                        "parameter 'asd' value is invalid: '', " +
                        "parameter 'zxc' value is invalid: '', " +
                        "parameter 'qwe' value is invalid: '']",
                () -> validator.checkCustomQueryParameters(queryToMap("qwe=&asd=&zxc=")));

        shouldOk(() -> validator.checkCustomQueryParameters(queryToMap("qwe=1")));
    }

    @Test
    public void validateCheckUser() {
        when(registration.checkUser(anyString(), anyString())).thenAnswer(inv -> inv.getArgument(0));

        shouldError("Player id is invalid: 'email@gmail.com'",
                () -> validator.checkUser(user("email@gmail.com", "12345678901234567890")));

        shouldOk(() -> validator.checkUser(user("validPlayerId", "12345678901234567890")));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa'",
                () -> validator.checkUser(user("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa", "12345678901234567890")));

        shouldError("Player id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkUser(user("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "12345678901234567890")));

        shouldError("Player code is invalid: '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000'",
                () -> validator.checkUser(user("codePlayerId", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")));

        shouldError("Player id is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkUser(user("email#&*^#gmail%#&^*com", "12345678901234567890")));

        shouldError("Player code is invalid: '12dehgfsgfsdlfidfj90'",
                () -> validator.checkUser(user("validPlayerId", "12dehgfsgfsdlfidfj90")));

        shouldError("Player id is invalid: 'null'",
                () -> validator.checkUser(user(null, "12345678901234567890")));

        shouldError("Player code is invalid: 'null'",
                () -> validator.checkUser(user("validPlayerId", null)));

    }

    private Registration.User user(String id, String code) {
        return new Registration.User(){{
            setId(id);
            setCode(code);
        }};
    }
}