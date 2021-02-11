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


import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class GameServiceTest {
    
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        forGames(FirstGameType.class, SecondGameType.class);
        gameService.init();
    }

    private void forGames(Class... classes) {
        gameService = new GameServiceImpl() {

            {
                excludeGames = new String[0];
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
                gameService.getGameNames().toString());
    }

    @Test
    public void shouldGetSpritesNames() {
        assertEquals("{first=[none, wall, hero], second=[none, red, green, blue]}", 
                gameService.getSpritesNames().toString());
    }
    
    @Test
    public void shouldGetOnlyGameNames() {
        assertEquals("[first, second]", 
                gameService.getOnlyGameNames().toString());
    }
    
    @Test
    public void shouldGetSpritesValues() {
        assertEquals("{first=[ , ☼, ☺], second=[ , R, G, B]}", 
                gameService.getSpritesValues().toString());
    }
    
    @Test
    public void shouldGetSprites() {
        assertEquals("{first=[none= , wall=☼, hero=☺], second=[none= , red=R, green=G, blue=B]}", 
                gameService.getSprites().toString());
    }
    
    @Test
    public void shouldGetDefaultGame() {
        assertEquals("first", 
                gameService.getDefaultGame());
    }

    @Test
    public void shouldGetGame() {
        assertEquals(FirstGameType.class,
                gameService.getGame("first").getClass());

        assertEquals(SecondGameType.class,
                gameService.getGame("second").getClass());

        assertEquals(NullGameType.class,
                gameService.getGame("not-exists").getClass());
    }

    // TODO этот тест надо запускать с парамером mvn test -DallGames иначе не тянутся дипенденси игр а хотелось бы их чекнуть так же 
    @Test
    public void shouldGetPngForSprites() {
        // given
        forGames(new GameServiceImpl().findInPackage("com.codenjoy.dojo").toArray(new Class[0]));
        
        // when
        Map<String, List<String>> sprites = gameService.getSpritesNames();
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

        assertEquals(errors.toString().replace(',', '\n'), 
                true, errors.isEmpty());
    }

}
