package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.controller.AbstractControllerTest;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.controller.control.PlayerController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.PlayerServiceImplTest.setupTimeService;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

public class StatisticServiceTest extends AbstractControllerTest<String, Joystick> {

    @Autowired
    private PlayerController controller;

    @Autowired
    private StatisticService statistic;

    @Autowired
    private PlayerServiceImpl players;

    @MockBean
    private TimeService timeService;

    private String endpoint;

    private void setup(String endpoint) {
        this.endpoint = endpoint;

        super.setup();

        createPlayer("player1", "room", "first");
        createPlayer("player2", "room", "first");
        createPlayer("player3", "room", "first");

        replyToServerImmediately(true);

        with.login.asNone();

        setupTimeService(timeService);
    }

    @Override
    protected String endpoint() {
        return endpoint;
    }

    @Override
    protected Controller<String, Joystick> controller() {
        return controller;
    }

    @Test
    public void shouldRequestControlsCount() {
        // given
        setup("ws");

        client(0).willAnswer("LEFT").start();
        client(1).willAnswer("RIGHT").start();

        // when
        players.tick();
        waitForServerReceived(false);

        // then
        assertEquals("StatisticService(time=timeService bean, \n" +
                        "tick=3000, \n" +
                        "tickTime=1970-01-01T02:00:03.000+0200, \n" +
                        "tickDuration=3000, \n" +
                        "screenUpdatesCount=0, \n" +
                        "requestControlsCount=2, \n" +
                        "dealsCount=3)",
                split(statistic.toString(), ", \n"));

        // given
        client(2).willAnswer("UP").start();

        // when
        players.tick();
        waitForServerReceived(false);

        // then
        assertEquals("StatisticService(time=timeService bean, \n" +
                        "tick=9000, \n" +
                        "tickTime=1970-01-01T02:00:09.000+0200, \n" +
                        "tickDuration=3000, \n" +
                        "screenUpdatesCount=0, \n" +
                        "requestControlsCount=1, \n" +
                        "dealsCount=3)",
                split(statistic.toString(), ", \n"));
    }

    @Test
    public void shouldScreenUpdatesCount() {
        // given
        setup("screen-ws");

        client(0).start();
        requestScreenData(0);
        client(1).start();
        requestScreenData(1);

        // when
        players.tick();
        waitForServerReceived(false);

        // then
        assertEquals("StatisticService(time=timeService bean, \n" +
                        "tick=3000, \n" +
                        "tickTime=1970-01-01T02:00:03.000+0200, \n" +
                        "tickDuration=3000, \n" +
                        "screenUpdatesCount=2, \n" +
                        "requestControlsCount=0, \n" +
                        "dealsCount=3)",
                split(statistic.toString(), ", \n"));

        // given
        client(2).start();
        requestScreenData(2);

        // when
        players.tick();
        waitForServerReceived(false);

        // then
        assertEquals("StatisticService(time=timeService bean, \n" +
                        "tick=9000, \n" +
                        "tickTime=1970-01-01T02:00:09.000+0200, \n" +
                        "tickDuration=3000, \n" +
                        "screenUpdatesCount=1, \n" +
                        "requestControlsCount=0, \n" +
                        "dealsCount=3)",
                split(statistic.toString(), ", \n"));
    }

    private void requestScreenData(int index) {
        client(index).sendToServer("{'name':getScreen, 'allPlayersScreen':true, " +
                "'players':[], 'room':'room'}");
    }

}
