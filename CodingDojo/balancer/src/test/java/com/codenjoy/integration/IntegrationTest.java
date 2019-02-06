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


import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.GameServers;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.dao.GameServer;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.integration.mocker.SpringMockerJettyRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.testng.Assert.assertEquals;

public class IntegrationTest {

    private TimerService timer;
    private ConfigProperties config;
    private Players players;
    private Scores scores;
    private GameServers gameServers;
    private GameServer game;

    private SpringMockerJettyRunner runner;
    private String context;
    private int port;
    private RestTemplate rest;

    @BeforeTest
    public void setupJetty() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "/appcontext"){{
            mockBean("gameServer");
            spyBean("gameServers");
            spyBean("configProperties");
            spyBean("players");
            spyBean("scores");
        }};

        port = runner.start(new Random().nextInt(1000) + 10000);

        rest = new RestTemplate();

        context = runner.getUrl();
        System.out.println(context);

        timer = runner.getBean(TimerService.class, "timerService");
        config = runner.getBean(ConfigProperties.class, "configProperties");
        players = runner.getBean(Players.class, "players");
        scores = runner.getBean(Scores.class, "scores");
        gameServers = runner.getBean(GameServers.class, "gameServers");
        game = runner.getBean(GameServer.class, "gameServer");
        timer.resume();
    }

    @BeforeMethod
    public void clean() {
        players.removeAll();
        scores.removeAll();
    }


    @Test
    public void test(){
        doReturn("code").when(game).createNewPlayer(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        assertPost("/rest/register",

                "{" +
                "  'email':'test@gmail.com'," +
                "  'firstName':'Stiven'," +
                "  'lastName':'Pupkin'," +
                "  'password':'13cf481db24b78c69ed39ab8663408c0'," +
                "  'code':'12345678901234567890'," +
                "  'city':'city'," +
                "  'skills':'Si Senior'," +
                "  'comment':'no comment'" +
                "}",

                "{\n" +
                "  'code':'code',\n" +
                "  'email':'test@gmail.com',\n" +
                "  'id':'btcfedopmomo66einibynbe',\n" +
                "  'server':'localhost:8080'\n" +
                "}");
    }

    private void assertPost(String url, String json, String answer) {
        String result = rest.postForObject(
                context + url,
                jsonRequest(json),
                String.class);

        assertJson(result, answer);
    }

    private void assertJson(String actual, String expected) {
        assertEquals(fix2(JsonUtils.prettyPrint(actual)), expected);
    }

    private HttpEntity jsonRequest(String json) {
        return new HttpEntity(fix(json), new HttpHeaders(){{
                setContentType(MediaType.APPLICATION_JSON);
            }});
    }

    private String fix(String json) {
        return json.replace("'", "\"");
    }

    private String fix2(String json) {
        return json.replace("\"", "'");
    }

}
