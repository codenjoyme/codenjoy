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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomServiceTest {

    private RoomService service;

    @Before
    public void setUp() {
        service = new RoomService();
    }

    @Test
    public void shouldRoomByDefault_isActive() {
        assertEquals(true, service.isActive("room"));
    }

    @Test
    public void shouldChangeRoomActiveness() {
        // when
        service.setActive("room", false);

        // then
        assertEquals(false, service.isActive("room"));

        // when
        service.setActive("room", true);

        // then
        assertEquals(true, service.isActive("room"));
    }
}