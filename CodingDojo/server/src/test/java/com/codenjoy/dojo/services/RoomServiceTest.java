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
import com.codenjoy.dojo.services.room.RoomState;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER1;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
        // given
        assertEquals(false, service.exists("non-exists-room"));

        // when then
        // комната которой не существует неактивна
        assertEquals(false, service.isActive("non-exists-room"));
    }

    @Test
    public void shouldRoomRegistrationIsClosed_ifNotPresent() {
        // given
        assertEquals(false, service.exists("non-exists-room"));

        // when then
        // комната которой не существует должна быть открыта для регистрации
        // иначе не создастся первый пользователь и не создаст эту комнату
        assertEquals(true, service.isOpened("non-exists-room"));
    }

    @Test
    public void shouldRoomIsActiveByDefault_ifPresent() {
        // given
        service.create("room", game1);

        // when then
        assertEquals(true, service.isActive("room"));
    }

    @Test
    public void shouldRoomRegistrationIsOpenedByDefault_ifPresent() {
        // given
        service.create("room", game1);

        // when then
        assertEquals(true, service.isOpened("room"));
    }

    @Test
    public void shouldReturnNewGameType_whenCreated() {
        // when
        GameType game = service.create("room", game1);

        // then
        assertEquals("RoomGameType{type=GameType[first], " +
                        "settings=First[Parameter 1=15, Parameter 2=true]}",
                game.toString());

        assertEquals("GameType[first]",
                game1.toString());
    }

    @Test
    public void shouldDoNotCreateNew_whenCreateAgain() {
        // given
        service.create("room", game1);

        // when
        GameType game = service.create("room", game1);

        // then
        assertEquals(true, service.isActive("room"));
        assertEquals(true, service.isOpened("room"));

        assertEquals("RoomGameType{type=GameType[first], " +
                        "settings=First[Parameter 1=15, Parameter 2=true]}",
                game.toString());

        assertEquals("GameType[first]",
                game1.toString());
    }

    @Test
    public void shouldNoEffect_whenSetActiveOfNonExistsRoom() {
        // when
        service.setActive("room", true);

        // then
        assertEquals(false, service.isActive("room"));
    }

    @Test
    public void shouldNoEffect_whenSetRegistrationOpenedOfNonExistsRoom() {
        // given
        // ее нет но она открыта
        assertEquals(true, service.isOpened("room"));
        assertEquals(false, service.exists("room"));

        // when
        // пробуем закрыть
        service.setOpened("room", false);

        // then
        // а нечего закрывать так как нет комнаты )
        assertEquals(true, service.isOpened("room"));
        assertEquals(false, service.exists("room"));
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
    public void shouldChangeRoomRegistrationStatus() {
        // given
        service.create("room", mock(GameType.class));

        // when
        service.setOpened("room", false);

        // then
        assertEquals(false, service.isOpened("room"));

        // when
        service.setOpened("room", true);

        // then
        assertEquals(true, service.isOpened("room"));
    }

    @Test
    public void shouldGetState_ifCreated() {
        // given
        service.create("room", game1);

        // when then
        assertEquals("RoomState(name=room, " +
                        "type=RoomGameType{type=GameType[first], " +
                        "settings=First[Parameter 1=15, Parameter 2=true]}, " +
                        "active=true, " +
                        "opened=true, " +
                        "tick=0)",
                service.state("room").get().toString());
    }

    @Test
    public void shouldGetState_ifNotCreated() {
        // when then
        assertEquals(false, service.state("room").isPresent());
    }

    @Test
    public void shouldGetSettings_ifCreated() {
        // given
        service.create("room", game1);

        // when then
        assertEquals("First[Parameter 1=15, Parameter 2=true]",
                service.settings("room").toString());
    }

    @Test
    public void shouldGetSettings_ifNotCreated() {
        // when then
        assertEquals(null, service.settings("room"));
    }

    @Test
    public void shouldGetGameType_ifCreated() {
        // given
        service.create("room", game1);

        // when then
        assertEquals("RoomGameType{type=GameType[first], settings=First[Parameter 1=15, Parameter 2=true]}",
                service.gameType("room").toString());
    }

    @Test
    public void shouldGetGameType_ifNotCreated() {
        // when then
        assertEquals(null, service.gameType("room"));
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
        assertEquals("First[Parameter 1=23, Parameter 2=true]",
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
        assertEquals("First[Parameter 1=23, Parameter 2=true]",
                settings1.toString());

        assertEquals("First[Parameter 1=15, Parameter 2=true]",
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
        assertEquals("First[Parameter 1=23, Parameter 2=true]",
                settings1.toString());

        assertEquals("Second[Parameter 3=43, Parameter 4=true]",
                settings2.toString());
    }

    @Test
    public void shouldGetRoomNames_withSorting() {
        // given
        service.create("room4", game2);
        service.create("room2", game1);
        service.create("room1", game1);
        service.create("room3", game2);

        // when then
        assertEquals("[room1, room2, room3, room4]", service.names().toString());
    }

    @Test
    public void shouldRemoveAll() {
        // given
        service.create("room4", game2);
        service.create("room2", game1);
        service.create("room1", game1);
        service.create("room3", game2);

        // when
        service.removeAll();

        // then
        assertEquals("[]", service.names().toString());
    }

    @Test
    public void shouldGetAllStates() {
        // given
        service.create("room4", game2);
        service.create("room2", game1);
        service.create("room1", game1);
        service.create("room3", game2);

        // when
        Collection<RoomState> all = service.all();

        // then
        assertEquals("[RoomState(name=room1, type=RoomGameType{type=GameType[first], settings=First[Parameter 1=15, Parameter 2=true]}, active=true, opened=true, tick=0), " +
                "RoomState(name=room2, type=RoomGameType{type=GameType[first], settings=First[Parameter 1=15, Parameter 2=true]}, active=true, opened=true, tick=0), " +
                "RoomState(name=room3, type=RoomGameType{type=GameType[second], settings=Second[Parameter 3=43, Parameter 4=true]}, active=true, opened=true, tick=0), " +
                "RoomState(name=room4, type=RoomGameType{type=GameType[second], settings=Second[Parameter 3=43, Parameter 4=true]}, active=true, opened=true, tick=0)]", all.toString());
    }

    @Test
    public void shouldExists() {
        // given
        service.create("room1", game1);
        service.create("room2", game2);

        // when then
        assertEquals(true, service.exists("room1"));
        assertEquals(true, service.exists("room2"));

        assertEquals(false, service.exists("room3"));
        assertEquals(false, service.exists(null));
        assertEquals(false, service.exists("null"));
        assertEquals(false, service.exists(""));
    }

    @Test
    public void shouldGetGameName() {
        // given
        service.create("room1", game1);
        service.create("room2", game1);
        service.create("room3", game2);
        service.create("room4", game2);

        // when then
        assertEquals("first", service.game("room1"));
        assertEquals("first", service.game("room2"));
        assertEquals("second", service.game("room3"));
        assertEquals("second", service.game("room4"));
    }

    @Test
    public void shouldGetGameRooms() {
        // given
        service.create("room3", game2);
        service.create("room4", game2);
        service.create("room1", game1);
        service.create("room2", game1);

        // when then
        assertEquals("[GameRooms(game=first, rooms=[room1, room2]), " +
                "GameRooms(game=second, rooms=[room3, room4])]",
                service.gameRooms().toString());
    }

    @Test
    public void shouldTickAllGames_isolatedForRooms() {
        // given
        service.create("room4", game2);
        service.create("room2", game1);
        service.create("room1", game1);
        service.create("room3", game2);

        assertTicks("[0, 0, 0, 0]");

        // when
        IntStream.range(0, 5).forEach(i -> {
            service.tick("room1");
            service.tick("room2");
            service.tick("room4");
        });
        service.tick("room2");
        service.tick("room2");

        // then
        assertTicks("[5, 7, 0, 5]");
    }

    @Test
    public void shouldResetAllTicks() {
        // given
        shouldTickAllGames_isolatedForRooms();

        assertTicks("[5, 7, 0, 5]");

        // when
        service.resetTick("room2");

        // then
        assertTicks("[5, 0, 0, 5]");
    }

    @Test
    public void shouldGetTick() {
        // given
        shouldTickAllGames_isolatedForRooms();

        assertTicks("[5, 7, 0, 5]");

        // when then
        assertEquals(5, service.getTick("room1"));
        assertEquals(7, service.getTick("room2"));
        assertEquals(0, service.getTick("room3"));
        assertEquals(5, service.getTick("room4"));
    }

    public void assertTicks(String expected) {
        assertEquals(expected, service.all().stream()
                .map(state -> state.getTick())
                .collect(toList()).toString());
    }
}