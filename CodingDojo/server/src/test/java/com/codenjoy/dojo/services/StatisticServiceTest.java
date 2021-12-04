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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.PlayerServiceImplTest.setupTimeService;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

public class StatisticServiceTest extends AbstractControllerTest<String, Joystick> {

    public static final String INITIAL_REQUEST = "any data";

    @Autowired
    private PlayerController controller;

    @Autowired
    private StatisticService statistic;

    @Autowired
    private PlayerServiceImpl players;

    @MockBean
    private TimeService timeService;

    @Before
    public void setup() {
        super.setup();

        createPlayer("player1", "room", "first");
        createPlayer("player2", "room", "first");
        createPlayer("player3", "room", "first");

        replyToServerImmediately(true);

        with.login.asNone();
    }

    @Override
    protected String endpoint() {
        return "ws";
    }


    @Override
    protected Controller<String, Joystick> controller() {
        return controller;
    }

    @Test
    public void shouldRequestControlsCount() {
        // given
        setupTimeService(timeService);
        client(0).willAnswer("LEFT").start();
        client(1).willAnswer("RIGHT").start();

        // when
        players.tick();
        waitForServerReceived(false);

        // then
        assertEquals("StatisticService(time=timeService bean, \n" +
                        "tick=1000, \n" +
                        "tickTime=1970-01-01T02:00:01.000+0200, \n" +
                        "tickDuration=2000, \n" +
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
                        "tick=7000, \n" +
                        "tickTime=1970-01-01T02:00:07.000+0200, \n" +
                        "tickDuration=2000, \n" +
                        "screenUpdatesCount=0, \n" +
                        "requestControlsCount=1, \n" +
                        "dealsCount=3)",
                split(statistic.toString(), ", \n"));
    }

}
