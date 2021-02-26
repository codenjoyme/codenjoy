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

import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER1;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RoomServiceTest {

    private RoomService service;
    private GameType game1;
    private GameType game2;

    @Before
    public void setUp() {
        // given
        service = new RoomService();
        game1 = new FirstGameType();
        game2 = new SecondGameType();
    }

    @Test
    public void shouldRoomIsNotActive_ifNotPresent() {
        // when then
        assertEquals(false, service.isActive("non-exists-room"));
    }

    @Test
    public void shouldRoomIsActiveByDefault_ifPresent() {
        // given
        service.create("room", game1);

        // when then
        assertEquals(true, service.isActive("room"));
    }

    @Test
    public void shouldCreateAgain_whenCreated() {
        // given
        service.create("room", game1);

        // when
        service.create("room", game1);

        // then
        assertEquals(true, service.isActive("room"));
    }

    @Test
    public void shouldException_whenSetActiveOfNonExistsRoom() {
        // when
        service.setActive("room", true);

        // then
        assertEquals(false, service.isActive("room"));
    }

    @Test
    public void shouldChangeRoomActiveness() {
        // given
        service.create("room", mock(GameType.class));

        // when
        service.setActive("room", false);

        // then
        assertEquals(false, service.isActive("room"));

        // when
        service.setActive("room", true);

        // then
        assertEquals(true, service.isActive("room"));
    }

    @Test
    public void shouldGetState_ifCreated() {
        // given
        service.create("room", game1);

        // when then
        assertEquals("RoomState(name=room, " +
                        "type=RoomGameType{type=GameType[first], " +
                        "settings=First-SettingsImpl(map={" +
                            "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                            "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})}, " +
                        "active=true)",
                service.state("room").toString());
    }

    @Test
    public void shouldGetState_ifNotCreated() {
        // when then
        assertEquals(null, service.state("room"));
    }

    @Test
    public void shouldGetSettings_ifCreated() {
        // given
        service.create("room", game1);

        // when then
        assertEquals("First-SettingsImpl(map={" +
                        "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})",
                service.settings("room").toString());
    }

    @Test
    public void shouldGetSettings_ifNotCreated() {
        // when then
        assertEquals(null, service.settings("room"));
    }

    @Test
    public void shouldAllSettings_isSame_inOneRoom() {
        // given
        service.create("room", game1);

        // when
        // получаем сеттинги из одной комнаты
        Settings settings1 = service.settings("room");
        Settings settings2 = service.settings("room");

        // then
        // они идекнтичны по наполнению
        assertEquals(settings1.toString(), settings2.toString());

        // when
        // меняем настройку в одном сеттинг объекте
        settings1.getParameter(PARAMETER1.key()).update(23);

        // then
        // проверили что поменялось в другом
        assertEquals("First-SettingsImpl(map={" +
                        "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[23]], " +
                        "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})",
                settings2.toString());
        // при этом объекты все так же равны
        assertEquals(settings1.toString(), settings2.toString());
    }

    @Test
    public void shouldAllSettings_isNotSame_inSeparateRooms() {
        // given
        service.create("room1", game1);
        service.create("room2", game1); // другая комната той же игры

        // when
        // получаем сеттинги из одной комнаты
        Settings settings1 = service.settings("room1");
        Settings settings2 = service.settings("room2");
        Settings settings3 = service.settings("room1"); // первая комната

        // then
        // они пока что идентичны по наполнению
        assertEquals(settings1.toString(), settings2.toString());
        assertEquals(settings1.toString(), settings3.toString());

        // when
        // меняем настройку в одном сеттинг объекте
        settings1.getParameter(PARAMETER1.key()).update(23);

        // then
        // проверили что поменялось только в нем
        assertEquals("First-SettingsImpl(map={" +
                        "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[23]], " +
                        "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})",
                settings1.toString());

        assertEquals("First-SettingsImpl(map={" +
                        "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})",
                settings2.toString());
    }

    @Test
    public void shouldAllSettings_isNotSame_inSeparateGames() {
        // given
        service.create("room1", game1);
        service.create("room2", game2); // другая комната другой игры

        // when
        // получаем сеттинги из одной комнаты
        Settings settings1 = service.settings("room1");
        Settings settings2 = service.settings("room2");

        // then
        // они изначально различны по наполнению
        assertNotEquals(settings1.toString(), settings2.toString());

        // when
        // меняем настройку в одном сеттинг объекте
        settings1.getParameter(PARAMETER1.key()).update(23);

        // then
        // проверили что поменялось только в нем
        assertEquals("First-SettingsImpl(map={" +
                        "Parameter 1=[Parameter 1:Integer = multiline[false] def[12] val[23]], " +
                        "Parameter 2=[Parameter 2:Boolean = def[true] val[true]]})",
                settings1.toString());

        assertEquals("Second-SettingsImpl(map={" +
                        "Parameter 3=[Parameter 3:Integer = multiline[false] def[43] val[43]], " +
                        "Parameter 4=[Parameter 4:Boolean = def[false] val[true]]})",
                settings2.toString());
    }
}