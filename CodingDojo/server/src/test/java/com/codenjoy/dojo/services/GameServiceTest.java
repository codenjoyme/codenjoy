package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.room.RoomService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER1;
import static com.codenjoy.dojo.services.mocks.SecondGameSettings.Keys.PARAMETER3;
import static com.codenjoy.dojo.services.mocks.SecondGameSettings.Keys.PARAMETER4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

// TODO try @SpringBootTest
public class GameServiceTest {

    private RoomService roomService;
    private GameServiceImpl service;

    @Before
    public void setup() {
        forGames(FirstGameType.class, SecondGameType.class);
        service.init();
    }

    private void forGames(Class... classes) {
        service = new GameServiceImpl() {

            {
                excludeGames = new String[0];
                roomService = GameServiceTest.this.roomService = new RoomService();
            }

            @Override
            public Collection<? extends Class> findInPackage(String packageName) {
                return Arrays.asList(classes);
            }
        };
    }

    @Test
    public void shouldGetGameNames() {
        assertEquals("[first, second]", 
                service.getGames().toString());
    }

    @Test
    public void shouldGetNullGame_whenEmptyGameName_caseGameNameOnly() {
        assertEquals(NullGameType.INSTANCE, service.getGameType(null));
        assertEquals(NullGameType.INSTANCE, service.getGameType("null"));
        assertEquals(NullGameType.INSTANCE, service.getGameType("not-exists"));
    }

    @Test
    public void shouldGetNullGame_whenEmptyGameName_caseGameAndRoomNames() {
        assertEquals(NullGameType.INSTANCE, service.getGameType(null, "valid-room"));
        assertEquals(NullGameType.INSTANCE, service.getGameType("null", "valid-room"));
        assertEquals(NullGameType.INSTANCE, service.getGameType("not-exists", "valid-room"));

        assertEquals(NullGameType.INSTANCE, service.getGameType("first", null));
        assertEquals(NullGameType.INSTANCE, service.getGameType("first", "null"));
        assertNotEquals(NullGameType.INSTANCE, service.getGameType("first", "non-exists")); // valid room and game
    }

    @Test
    public void shouldGetNullGame_whenEmptyGameName() {
        roomService.create("room1", service.getGameType("first"));
        roomService.create("room2", service.getGameType("second"));
        roomService.create("room3", service.getGameType("second"));

        assertEquals("[first, room1, room2, room3, second]",
                service.getRooms().toString());
    }

    @Test
    public void shouldGetSpritesNames() {
        assertEquals("{first=[none, wall, hero], second=[none, red, green, blue]}", 
                service.getSpritesNames().toString());
    }
    
    @Test
    public void shouldGetOnlyGameNames() {
        assertEquals("[first, second]", 
                service.getOnlyGames().toString());
    }
    
    @Test
    public void shouldGetSpritesValues() {
        assertEquals("{first=[ , ☼, ☺], second=[ , R, G, B]}", 
                service.getSpritesValues().toString());
    }
    
    @Test
    public void shouldGetSprites() {
        assertEquals("{first=[none= , wall=☼, hero=☺], second=[none= , red=R, green=G, blue=B]}", 
                service.getSprites().toString());
    }
    
    @Test
    public void shouldGetDefaultGame() {
        // по умолчанию так же создаются комнаты first & second
        roomService.create("room1", service.getGameType("first"));
        roomService.create("room2", service.getGameType("second"));
        roomService.create("room3", service.getGameType("second"));

        assertEquals("first",
                service.getDefaultRoom());
    }

    @Test
    public void shouldGetGame() {
        assertEquals(FirstGameType.class,
                service.getGameType("first").getClass());

        assertEquals(SecondGameType.class,
                service.getGameType("second").getClass());

        assertEquals(NullGameType.class,
                service.getGameType("not-exists").getClass());
    }

    // TODO этот тест надо запускать с парамером mvn test -DallGames иначе не тянутся дипенденси игр а хотелось бы их чекнуть так же 
    @Test
    public void shouldGetPngForSprites() {
        // given
        forGames(new GameServiceImpl().findInPackage("com.codenjoy.dojo").toArray(new Class[0]));
        
        // when
        Map<String, List<String>> sprites = service.getSpritesNames();
        System.out.println(sprites.toString());
        
        // then
        List<String> errors = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : sprites.entrySet()) {
            for (String sprite : entry.getValue()) {
                String spriteUri = String.format("/%s/%s.png", entry.getKey(), sprite);
                File file = new File("target/test-classes/sprite" + spriteUri);
                if (!file.exists() && !new File("/sprite" + spriteUri).exists()) {
                    errors.add("Файл не найден: " + file.getAbsolutePath());
                }
            }
        }

        assertEquals(split(errors, "\n,"),
                true, errors.isEmpty());
    }

    @Test
    public void shouldSameSettings_whenGetGameByRoomName() {
        // given
        List<GameType> list = new LinkedList<>(){{
            add(service.getGameType("first", "room1"));
            add(service.getGameType("first", "room1"));

            add(service.getGameType("first", "room2"));

            add(service.getGameType("second", "room3"));
            add(service.getGameType("second", "room3"));

            add(service.getGameType("second", "room4"));
            add(service.getGameType("second", "room4"));

            add(service.getGameType("first"));

            add(service.getGameType("second"));
        }};

        // then
        assertEquals("First[Parameter 1=15, Parameter 2=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(0).getSettings().parameter(PARAMETER1.key()).update(123);

        // then
        assertEquals("First[Parameter 1=123, Parameter 2=true]\n" +
                        "First[Parameter 1=123, Parameter 2=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(1).getSettings().parameter(PARAMETER1.key()).update(234);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(2).getSettings().parameter(PARAMETER1.key()).update(345);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(3).getSettings().parameter(PARAMETER4.key()).update(false);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=false]\n" +
                        "Second[Parameter 3=43, Parameter 4=false]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(4).getSettings().parameter(PARAMETER3.key()).update(456);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(5).getSettings().parameter(PARAMETER3.key()).update(567);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=true]\n" +
                        "Second[Parameter 3=567, Parameter 4=true]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(6).getSettings().parameter(PARAMETER4.key()).update(false);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(7).getSettings().parameter(PARAMETER1.key()).update(678);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        // TODO сделать так, чтобы изменение базовых настроек
                        //  влияло на будущие созданные настройки комнат
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

        // when
        list.get(8).getSettings().parameter(PARAMETER3.key()).update(789);

        // then
        assertEquals("First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=234, Parameter 2=true]\n" +
                        "First[Parameter 1=345, Parameter 2=true]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=456, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        "Second[Parameter 3=567, Parameter 4=false]\n" +
                        "First[Parameter 1=15, Parameter 2=true]\n" +
                        // TODO сделать так, чтобы изменение базовых настроек
                        //  влияло на будущие созданные настройки комнат
                        "Second[Parameter 3=43, Parameter 4=true]\n",
                toString(list));

    }

    private String toString(List<GameType> list) {
        return list.stream()
                .map(GameType::getSettings)
                .map(settings -> settings.toString())
                .reduce("", (out, string) -> out.concat(string + "\n"));
    }

    @Test
    public void shouldGetDefaultProgress() {
        assertEquals("{'levelProgress':{'total':1,'current':1,'lastPassed':0}}",
                service.getDefaultProgress(service.getGameType("first")));

        assertEquals("{'levelProgress':{'total':10,'current':1,'lastPassed':0}}",
                service.getDefaultProgress(service.getGameType("second")));
    }
}
