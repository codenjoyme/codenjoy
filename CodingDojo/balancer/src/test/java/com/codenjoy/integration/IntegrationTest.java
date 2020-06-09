package com.codenjoy.integration;

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


import com.codenjoy.dojo.BalancerApplication;
import com.codenjoy.dojo.conf.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.GameServer;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.httpclient.SmsGatewayClient;
import com.codenjoy.dojo.services.properties.SmsProperties;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.web.security.SecurityContextAuthenticator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = BalancerApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@WebAppConfiguration
public class IntegrationTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TimerService timer;

    @SpyBean
    private ConfigProperties config;

    @Autowired
    private SmsProperties smsProperties;

    @SpyBean
    private Players players;

    @SpyBean
    private Scores scores;

    @SpyBean
    private GameServers gameServers;

    @SpyBean
    private RegistrationService registration;

    @SpyBean
    private SmsService sms;

    @MockBean
    private SmsGatewayClient gateway;

    @SpyBean
    private PasswordEncoder passwordEncoder;

    @SpyBean
    private DebugService debug;

    @SpyBean
    private GameServer game;

    @SpyBean
    private SecurityContextAuthenticator authenticator;

    private String verificationCode;

    @Before
    public void setup() {
        timer.resume();

        debug.setDebugEnable(false);

        smsProperties.setEnabled(false);
        verificationCode = "123456";

        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        config.getAdminLogin(),
                        config.getAdminPassword()
                ));
    }

    public void clean() {
        players.removeAll();
        scores.removeAll();
        resetMocks();
    }

    @After
    public void after() {
        resetMocks();
    }

    @SneakyThrows
    protected String mapToJson(Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @SneakyThrows
    protected <T> T mapFromJson(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, clazz);
    }

    @SneakyThrows
    private String get(String uri) {
        return process(200, MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    private String get(int status, String uri) {
        return process(status, MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    private String post(int status, String uri) {
        return process(status, MockMvcRequestBuilders.post(uri));
    }

    @SneakyThrows
    private String post(String uri) {
        return process(200, MockMvcRequestBuilders.post(uri));
    }

    @SneakyThrows
    private String post(int status, String uri, String data) {
        return process(status, MockMvcRequestBuilders.post(uri, data)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    private String process(int status, MockHttpServletRequestBuilder post) throws Exception {
        MvcResult mvcResult = mvc.perform(post
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(result, status, mvcResult.getResponse().getStatus());
        return result;
    }

    private void assertException(String expected, Runnable supplier) {
        try {
            supplier.run();
            fail("expected exception");
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

    private void assertError(String message, String source) {
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
    }

    private JSONObject tryParseAsJson(String source) {
        try {
            return new JSONObject(source);
        } catch (JSONException e) {
            return new JSONObject(){{
                put("message", source);
            }};
        }
    }

    @Test
    public void shouldSendSms_whenRegister() {
        // given
        smsProperties.setEnabled(true);

        // TODO#2 почему-то не срабатывает
        doReturn("123457").when(registration).generateVerificationCode();
        verificationCode = null; // TODO#2 жвачка в него потом запишется реальное сгенерированное значение

        // when
        shouldRegister_whenNotPresent();

        // then
        verify(sms).sendSmsTo("+380501234567", verificationCode, SmsService.SmsType.REGISTRATION);

        ArgumentCaptor<SmsService.SmsSendRequest> captor = ArgumentCaptor.forClass(SmsService.SmsSendRequest.class);
        verify(gateway).sendSms(captor.capture());
        assertEquals("SmsService.SmsSendRequest(operation=SENDSMS, " +
                "message=SmsService.Message(lifetime=4, recipient=+380501234567, " +
                "body=Vash kod pidtverdzennya reestratsui: " + verificationCode + ".\n" +
                "Codenjoy Team.))", captor.getValue().toString());
    }

    @Test
    public void shouldRegister_whenNotPresent() {
        // given
        clean();

        shouldCreateNewPlayerOnGame("generated-id", "12345678901234567890");
        passwordEncoded("password", "$2a$10$Enc0dEd2");

        // when
        assertPost("/rest/register",

                validPlayer(),

                "{\n" +
                "  'code':'12345678901234567890',\n" +
                "  'email':'test@gmail.com',\n" +
                "  'id':'generated-id',\n" +
                // номер нормализировался
                "  'phone':'+380501234567',\n" +
                "  'server':'localhost:8080'\n" +
                "}");

        // then
        verify(game).createNewPlayer(
                "localhost:8080",
                "generated-id",
                "12345678901234567890",
                "test@gmail.com",
                "+380501234567",
                "Stiven Pupkin",
                // raw password идет на game сервер, там он захешируется
                "password",
                "127.0.0.1",
                "0", // create new
                "{}");

        verify(players, times(1)).create(any(Player.class));

        assertPlayer();

        verifyLogin("test@gmail.com", "password");
    }

    private void passwordEncoded(String encoded, String decoded) {
        doReturn(decoded).when(passwordEncoder).encode(anyString());
        doReturn(true).when(passwordEncoder).matches(encoded, decoded);
    }

    @Test
    public void shouldSameRegisterFailed_whenPresent_byEmail() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCreateNewPlayerOnGame("generated-id", "12345678901234567890");

        // when
        assertPost(400, "/rest/register",

                "{" +
                // email тот же
                "  'email':'test@gmail.com'," +
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                // а телефон другой
                "  'phone':'0500000000'," +
                "  'password':'password'," +
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}",

                "IllegalArgumentException: User with this email is already registered");

        // then
        verify(players, never()).create(any(Player.class));

        // тут без изменений
        assertPlayer();
    }

    @Test
    public void shouldSameRegisterFailed_whenPresent_byPhone() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCreateNewPlayerOnGame("generated-id", "12345678901234567890");

        // when
        assertPost(400, "/rest/register",

                "{" +
                        // email другой
                        "  'email':'other-email@gmail.com'," +
                        "  'firstName':'Stiven'," +
                        "  'lastName':'Pupkin'," +
                        // но телефон тот же
                        "  'phone':'0501234567'," +
                        "  'password':'password'," +
                        "  'city':'city'," +
                        "  'skills':'Si Senior'," +
                        "  'comment':'no comment'" +
                        "}",

                "IllegalArgumentException: User with this phone number is already registered");

        // then
        verify(players, never()).create(any(Player.class));

        // тут без изменений
        assertPlayer();
    }

    private void assertPlayer() {
        Player player = players.get("generated-id");
        if (verificationCode == null) { // TODO#2 немного жвачки
            verificationCode = player.getVerificationCode();
        }
        assertEquals("Player{" +
                        "id='generated-id', " +
                        "email='test@gmail.com', " +
                        "phone='+380501234567', " +
                        "firstName='Stiven', " +
                        "lastName='Pupkin', " +
                        "password='$2a$10$Enc0dEd2', " +
                        "city='city', " +
                        "skills='Si Senior', " +
                        "comment='no comment', " +
                        "code='12345678901234567890', " +
                        "server='localhost:8080', " +
                        "approved=0, " +
                        "verificationCode='" + verificationCode + "', " +
                        "verificationType='REGISTRATION'}",
                player.toString());
    }

    @Test
    public void shouldRegisterError_whenGameServerIsNotResponding() {
        // given
        clean();
        debug.setDebugEnable(true);

        shouldCreateNewPlayerOnGame("generated-id", "12345678901234567890");
        shouldThrowWhenCreateNewPlayerOnGame(new RuntimeException("Shit happens"));

        // when
        assertPostError(500, "/rest/register",

                validPlayer(),

                "java.lang.RuntimeException: [At game server: RuntimeException: Shit happens]");

        // then
        verify(game).createNewPlayer(
                "localhost:8080",
                "generated-id",
                "12345678901234567890",
                "test@gmail.com",
                "+380501234567",
                "Stiven Pupkin",
                "password",
                "127.0.0.1",
                "0",
                "{}");

        verifyDoNothingOnPlayers();
    }

    private void verifyDoNothingOnPlayers() {
        verify(players, never()).create(any(Player.class));

        assertEquals(players.get("generated-id"),
                null);
    }

    private String validPlayer() {
        return "{" +
                "  'email':'test@gmail.com'," +
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                "  'phone':'0501234567'," +
                // пароль должен быть в чистом виде, потому что мы
                // авторизируемся так же в spring security
                "  'password':'password'," +
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}";
            }

    @Test
    public void shouldRegisterValidationError_whenBadEmail() {
        // given
        clean();

        // when
        assertPost(400, "/rest/register",

                "{" +
                "  'email':'NOT_EMAIL'," +    // there is an error
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                "  'phone':'0501234567'," +
                "  'password':'password'," +
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}",

                "IllegalArgumentException: Player email is invalid: NOT_EMAIL");

        // then
        verifyDoNothingOnRegistration();
    }

    @Test
    public void shouldRegisterValidationError_whenEmailIsNull() {
        // given
        clean();

        // when
        assertPost(400, "/rest/register",

                "{" +
                "  'email':null," +    // there is an error
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                "  'phone':'0501234567'," +
                "  'password':'password'," +
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}",

                "IllegalArgumentException: Player email is invalid: null");

        // then
        verifyDoNothingOnRegistration();
    }

    @Test
    public void shouldRegisterValidationError_whenPasswordIsNull() {
        // given
        clean();

        // when
        assertPost(400, "/rest/register",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                "  'phone':'0501234567'," +
                "  'password':null," +     // there is an error
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}",

                "IllegalArgumentException: Password string is empty: null");

        // then
        verifyDoNothingOnRegistration();
    }

    @Test
    public void shouldRegisterValidationError_whenOtherStringsIsNull() {
        // given
        clean();

        // when
        assertPost(400, "/rest/register",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'firstName':null," +        // there is an error
                "  'lastName':null," +         // there is an error
                "  'phone':'123'," +           // there is an error
                "  'password':'password'," +
                "  'city':null," +             // there is an error
                "  'skills':null," +           // there is an error
                "  'comment':null" +           // it's ok
                "}",

                "IllegalArgumentException: Something wrong with parameters on this request: " +
                        "[Invalid phone number: 123, " +
                        "FirstName string is empty: null, " +
                        "LastName string is empty: null, " +
                        "City string is empty: null, " +
                        "Skills string is empty: null]");

        // then
        verifyDoNothingOnRegistration();
    }

    private void verifyDoNothingOnRegistration() {
        verifyNoMoreInteractions(game);

        verifyDoNothingOnPlayers();

        verifyNoMoreInteractions(authenticator);
    }

    @Test
    public void shouldUnSuccessfulLogin_whenNotVerified() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCheckIfExistsOnGame(true);

        assertPlayerApproved(0);

        // when
        assertPostError(401, "/rest/login",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'password':'password'" +
                "}",

                "LoginException: User is not verified");

        // then
        verifyNoMoreInteractions(game);
        verifyNoMoreInteractions(authenticator);
    }

    @Test
    public void shouldUnSuccessfulLogin_whenNotEmailFound() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCheckIfExistsOnGame(true);

        assertPlayerApproved(0);

        // when
        assertPostError(401, "/rest/login",

                "{" +
                "  'email':'bad-email@gmail.com'," +  // не тот имейл
                "  'password':'password'" +
                "}",

                "LoginException: User with this email not found");

        // then
        verifyNoMoreInteractions(game);
        verifyNoMoreInteractions(authenticator);
    }

    @Test
    public void shouldUnSuccessfulLogin_whenBadPassword() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCheckIfExistsOnGame(true);

        confirmRegistration();

        // when
        assertPostError(401, "/rest/login",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'password':'BAD-password'" +  // не тот пароль
                "}",

                "LoginException: Wrong password/code for this email");

        // then
        verifyNoMoreInteractions(game);
        verifyNoMoreInteractions(authenticator);
    }

    @Test
    public void shouldSuccessfulLogin_whenCodeInsteadOfPassword() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCheckIfExistsOnGame(true);

        confirmRegistration();

        // when
        assertPost("/rest/login",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'code':'12345678901234567890'," +  // по коду можно залогиниться
                "  'password':'BAD-password'" +       // но пароль надо хоть какой-то указать, пусть не верный
                "}",

                "{\n" +
                "  'code':'12345678901234567890',\n" +
                "  'email':'test@gmail.com',\n" +
                "  'id':'generated-id',\n" +
                "  'phone':'+380501234567',\n" +
                "  'server':'localhost:8080'\n" +
                "}");

        // then
        verifyLogin("test@gmail.com", "password");
    }

    private void assertPlayerApproved(int expected) {
        assertEquals(expected, players.get("generated-id").getApproved());
    }

    @Test
    public void shouldSuccessfulLogin_afterVerification() {
        // given
        shouldRegister_whenNotPresent();
        resetMocks();

        shouldCheckIfExistsOnGame(true);
        passwordEncoded("password", "$2a$10$Enc0dEd2");
        confirmRegistration();

        // when
        assertPost("/rest/login",

                "{" +
                "  'email':'test@gmail.com'," +
                // пароль не должен быть хешированный
                "  'password':'password'" +
                "}",

                "{\n" +
                "  'code':'12345678901234567890',\n" +
                "  'email':'test@gmail.com',\n" +
                "  'id':'generated-id',\n" +
                "  'phone':'+380501234567',\n" +
                "  'server':'localhost:8080'\n" +
                "}");

        // then
        verify(game).existsOnServer("localhost:8080", "test@gmail.com");
        verifyLogin("test@gmail.com", "password");
    }

    private void confirmRegistration() {
        // given
        assertPlayerApproved(0);

        // when
        assertPost("/rest/register/confirm",

                "{" +
                "  'phone':'+380501234567'," +
                "  'code':'123456'" +
                "}",

                "{\n" +
                "  'code':'12345678901234567890',\n" +
                "  'email':'test@gmail.com',\n" +
                "  'id':'generated-id',\n" +
                "  'phone':'+380501234567',\n" +
                "  'server':'localhost:8080'\n" +
                "}");

        // then
        assertPlayerApproved(1);
    }

    private void verifyLogin(String email, String password) {
        verify(authenticator).login(any(HttpServletRequest.class), eq(email), eq(password));
    }

    @Test
    public void shouldExistOnGameServer_whenRegistered() {
        // given
        shouldRegister_whenNotPresent();

        shouldCheckIfExistsOnGame(true);

        // when
        assertGet("/rest/player/test@gmail.com/active/12345678901234567890",
                "true");

        // then
        verify(game).existsOnServer("localhost:8080", "test@gmail.com");
    }

    @Test
    public void shouldExitFromGameServer_whenRegistered() {
        // given
        shouldRegister_whenNotPresent();

        shouldExitFromGame(true);

        // when
        assertGet("/rest/player/test@gmail.com/exit/12345678901234567890",
                "true");

        // then
        verify(game).remove("localhost:8080", "test@gmail.com", "12345678901234567890");
    }

    @Test
    public void shouldJoinToGameServer_whenRegistered() {
        // given
        shouldSuccessfulLogin_afterVerification();

        shouldCheckIfExistsOnGame(false);

        // when
        assertGet("/rest/player/test@gmail.com/join/12345678901234567890",
                "true");

        // then
        verify(game).existsOnServer("localhost:8080", "test@gmail.com");
        verify(game).createNewPlayer(
                "localhost:8080",
                "generated-id",
                "12345678901234567890",
                "test@gmail.com",
                "+380501234567",
                "Stiven Pupkin",
                "password",
                "127.0.0.1",
                null, // try load from save
                null
        );
    }

    @Test
    public void shouldRemoveFromServer_whenRegistered() {
        // given
        shouldJoinToGameServer_whenRegistered();

        shouldExitFromGame(true);

        // when
        assertGet("/rest/remove/test@gmail.com/", "true");

        // then
        verify(game).remove("localhost:8080", "test@gmail.com", "12345678901234567890");
        verify(players).remove("test@gmail.com");
    }

    private void resetMocks() {
        verifyNoMoreInteractions(game, authenticator);

        reset(game, sms, players, scores, config,
                gameServers, registration,
                gateway, debug,
                passwordEncoder, authenticator);
    }

    private void shouldCreateNewPlayerOnGame(String id, String code) {
        doReturn(id).when(registration).generateId();

        doReturn(code).when(game).createNewPlayer(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private void shouldThrowWhenCreateNewPlayerOnGame(Exception exception) {
        doThrow(exception).when(game).createNewPlayer(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private void shouldCheckIfExistsOnGame(boolean exists) {
        doReturn(exists).when(game).existsOnServer(anyString(), anyString());
    }

    private void shouldExitFromGame(boolean exited) {
        doReturn(exited).when(game).remove(anyString(), anyString(), anyString());
    }

    private void assertPost(String url, String json, String expected) {
        assertPost(200, url, json, expected);
    }

    private void assertPost(int status, String url, String json, String expected) {
        String result = post(status, url, fix(json));
        assertJson(expected, result);
    }

    private void assertPostError(int status, String url, String json, String expectedMessage) {
        String result = post(status, url, fix(json));
        assertError(expectedMessage, result);
    }

    private void assertGet(String url, String expected) {
        String result = get(url);
        assertJson(expected, result);
    }

    private void assertJson(String expected, String actual) {
        boolean isJson = actual.startsWith("{");
        assertEquals(expected,
                isJson ? fix2(JsonUtils.prettyPrint(actual)) : actual);
    }

    private String fix(String json) {
        return json.replace("'", "\"");
    }

    private String fix2(String json) {
        return json.replace("\"", "'");
    }

}
