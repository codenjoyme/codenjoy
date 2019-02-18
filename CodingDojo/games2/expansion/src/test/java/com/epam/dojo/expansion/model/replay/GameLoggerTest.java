package com.epam.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.epam.dojo.expansion.model.AbstractSinglePlayersTest;
import com.epam.dojo.expansion.model.GameFactory;
import com.epam.dojo.expansion.model.GameRunnerWithLobbyTest;
import com.epam.dojo.expansion.model.MultipleGameFactory;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Oleksandr_Baglai on 2017-09-22.
 */
public class GameLoggerTest extends AbstractSinglePlayersTest {

    private File gameDataFolder;

    private MultipleGameFactory gameFactory;

    @Override
    protected GameFactory getGameFactory(LevelsFactory single, LevelsFactory multiple) {
        gameFactory = new MultipleGameFactory(dice, single, multiple);
        return gameFactory;
    }

    @Before
    public void setup() {
        super.setup();

        gameDataFolder = new File("gameData");
        if (gameDataFolder.exists()) {
            for (File file : gameDataFolder.listFiles()) {
                file.delete();
            }
        }
    }

    @Test
    public void shouldSaveStateToFile() {
        // given
        data.gameLoggingEnable(true);
        String multiple =
                "╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘";
        String single =
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘";
        givenFl(single, multiple);
        createPlayers(2);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        tickAll();

        tickAll();

        // then select different way
        hero(PLAYER1, 1, 2).down();
        hero(PLAYER2, 2, 3).right();
        tickAll();

        // then
        assertL(multiple, PLAYER1);
        assertL(multiple, PLAYER2);

        String layer2 =
                "-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);

        String forces =
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right();
        hero(PLAYER2, 3, 3).down();
        tickAll();

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B002-=#\n" +
                "-=#00B-=#001-=#\n" +
                "-=#002001-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // then
        String game1 = single(PLAYER1).getPlayer().getCurrent().id();
        File file = getFile("game-" + game1 + "-1.txt");

        String result = loadFromFile(file);

        String player1 = singles.get(PLAYER1).getPlayer().lg.id();
        String player2 = singles.get(PLAYER2).getPlayer().lg.id();
        String hero1 = hero(PLAYER1).lg.id();
        String hero2 = hero(PLAYER2).lg.id();

        assertEquals(("Game started\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "New player P@314c508a registered with hero H@10b48321 with base at '{\"x\":2,\"y\":3}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@314c508a'}\"\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║.2.│║1..│║..E│└───┘\",\"-------♦---♥-------------\",\"-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":1,\"y\":2}}],\"movements\":[{\"count\":1,\"direction\":\"DOWN\",\"region\":{\"x\":1,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":2,\"y\":3}}],\"movements\":[{\"count\":1,\"direction\":\"RIGHT\",\"region\":{\"x\":2,\"y\":3}}]}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║.2.│║1..│║..E│└───┘\",\"-------♦♦--♥----♥--------\",\"-=#-=#-=#-=#-=#-=#-=#00B001-=#-=#00B-=#-=#-=#-=#001-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":1,\"y\":1}}],\"movements\":[{\"count\":1,\"direction\":\"RIGHT\",\"region\":{\"x\":1,\"y\":1}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":3,\"y\":3}}],\"movements\":[{\"count\":1,\"direction\":\"DOWN\",\"region\":{\"x\":3,\"y\":3}}]}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║.2.│║1..│║..E│└───┘\",\"-------♦♦--♥-♦--♥♥-------\",\"-=#-=#-=#-=#-=#-=#-=#00B002-=#-=#00B-=#001-=#-=#002001-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n")
                            .replace("P@6c3f5566", player1)
                            .replace("P@314c508a", player2)
                            .replace("H@12405818", hero1)
                            .replace("H@10b48321", hero2)
                            .replace("E@6a396c1e", game1),
                result);
    }

    @Test
    public void shouldCreateNewFileWhenGoToExitOnMultiple() {
        // given
        data.gameLoggingEnable(true);
        String single =
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘";
        String multiple =
                "╔═══┐" +
                "║...│" +
                "║1.2│" +
                "║.E.│" +
                "└───┘";
        givenFl(single, multiple);
        createPlayers(2);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        tickAll();

        tickAll();

        // then select different way
        hero(PLAYER1, 1, 2).down();
        hero(PLAYER2, 3, 2).down();
        tickAll();

        // then
        assertL(multiple, PLAYER1);
        assertL(multiple, PLAYER2);

        String layer2 =
                "-----" +
                "-----" +
                "-♥-♦-" +
                "-♥-♦-" +
                "-----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);

        String forces =
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#00B-=#\n" +
                "-=#001-=#001-=#\n" +
                "-=#-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right();
        tickAll();

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#00B-=#\n" +
                "-=#002001001-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // TODO это проблема, надо чтобы если один плеер прошел карту выйдя на EXIT то обновились и все плеера!
        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#00B-=#\n" +
                "-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // then
        String game1 = single(PLAYER1).getPlayer().getCurrent().id();
        File file = getFile("game-" + game1 + "-1.txt");

        String result = loadFromFile(file);

        String player1 = singles.get(PLAYER1).getPlayer().lg.id();
        String player2 = singles.get(PLAYER2).getPlayer().lg.id();
        String hero1 = hero(PLAYER1).lg.id();
        String hero2 = hero(PLAYER2).lg.id();

        assertEquals(("Game started\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "New player P@314c508a registered with hero H@10b48321 with base at '{\"x\":3,\"y\":2}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@314c508a'}\"\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.E.│└───┘\",\"-----------♥-♦-----------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":1,\"y\":2}}],\"movements\":[{\"count\":1,\"direction\":\"DOWN\",\"region\":{\"x\":1,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":3,\"y\":2}}],\"movements\":[{\"count\":1,\"direction\":\"DOWN\",\"region\":{\"x\":3,\"y\":2}}]}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.E.│└───┘\",\"-----------♥-♦--♥-♦------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00B-=#00B-=#-=#001-=#001-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":1,\"y\":1}}],\"movements\":[{\"count\":1,\"direction\":\"RIGHT\",\"region\":{\"x\":1,\"y\":1}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.E.│└───┘\",\"-----------♥-♦--♥♥♦------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00B-=#00B-=#-=#002001001-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.E.│└───┘\",\"-----------♥-♦----♦------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00B-=#-=#-=#-=#001-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n")
                                .replace("P@6c3f5566", player1)
                                .replace("P@314c508a", player2)
                                .replace("H@12405818", hero1)
                                .replace("H@10b48321", hero2)
                                .replace("E@6a396c1e", game1),
                result);
    }

    @Test
    public void shouldCreateNewFileWhenSomebodyLoose() {
        // given
        data.gameLoggingEnable(true);
        String single =
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘";
        String multiple =
                "╔═══┐" +
                "║...│" +
                "║1.2│" +
                "║...│" +
                "└───┘";
        givenFl(single, multiple);
        createPlayers(2);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        tickAll();

        tickAll();

        // then select different way
        INCREASE = 10;
        MOVE = 10;
        hero(PLAYER1, 1, 2).right();
        tickAll();

        hero(PLAYER1, 2, 2).right();
        tickAll();

        // then
        assertL(multiple, PLAYER1);
        assertL(multiple, PLAYER2);

        String layer2 =
                "-----" +
                "-----" +
                "-♥♥--" +
                "-----" +
                "-----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);

        String forces =
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);

        // when
        tickAll();

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        INCREASE = 2;
        MOVE = 2;
        hero(PLAYER2, 3, 2).left();
        tickAll();

        assertE("-----" +
                "-----" +
                "-♥♦♦-" +
                "-----" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A00200A-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // then
        String game1 = single(PLAYER1).getPlayer().getCurrent().id();

        File file1 = getFile("game-" + game1 + "-1.txt");
        String result1 = loadFromFile(file1);

        String player1 = singles.get(PLAYER1).getPlayer().lg.id();
        String player2 = singles.get(PLAYER2).getPlayer().lg.id();
        String hero1 = hero(PLAYER1).lg.id();
        String hero2 = hero(PLAYER2).lg.id();

        assertEquals(("Game started\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "New player P@314c508a registered with hero H@10b48321 with base at '{\"x\":3,\"y\":2}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@314c508a'}\"\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥-♦-----------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":1,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":1,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♥♦-----------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":2,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":2,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♥------------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Game finished\n")
                        .replace("P@6c3f5566", player1)
                        .replace("P@314c508a", player2)
                        .replace("H@12405818", hero1)
                        .replace("H@10b48321", hero2)
                        .replace("E@6a396c1e", game1),
                result1);

        File file2 = getFile("game-" + game1 + "-2.txt");
        String result2 = loadFromFile(file2);

        // TODO иногда этот ассерт валится, но редко, почему?
        assertEquals(("Game started\n" +
                        "New player P@7e07db1f registered with hero H@1189dd52 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@35cabb2a-2','playerName':'P@7e07db1f'}\"\n" +
                        "New player P@36bc55de registered with hero H@564fabc8 with base at '{\"x\":3,\"y\":2}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@35cabb2a-2','playerName':'P@36bc55de'}\"\n" +
                        "Hero H@1189dd52 of player P@7e07db1f received command:'{}'\n" +
                        "Hero H@564fabc8 of player P@36bc55de received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥-♦-----------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@1189dd52 of player P@7e07db1f received command:'{}'\n" +
                        "Hero H@564fabc8 of player P@36bc55de received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":3,\"y\":2}}],\"movements\":[{\"count\":2,\"direction\":\"LEFT\",\"region\":{\"x\":3,\"y\":2}}]}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♦♦-----------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00200A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n")
                        .replace("P@7e07db1f", player1)
                        .replace("P@36bc55de", player2)
                        .replace("H@1189dd52", hero1)
                        .replace("H@564fabc8", hero2)
                        .replace("E@35cabb2a", game1),
                result2);
    }

    @Test
    public void shouldPrintHeroIsNotAliveWhen3PlayersAndOneIsDie() {
        // given
        data.gameLoggingEnable(true);
        String single =
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘";
        String multiple =
                "╔═══┐" +
                "║...│" +
                "║1.2│" +
                "║.3.│" +
                "└───┘";
        givenFl(single, multiple);
        createPlayers(3);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        hero(PLAYER3, 1, 3).right();
        tickAll();

        tickAll();

        // then select different way
        INCREASE = 10;
        MOVE = 10;
        hero(PLAYER1, 1, 2).right();
        tickAll();

        hero(PLAYER1, 2, 2).right();
        tickAll();

        // then
        assertL(multiple, PLAYER1);
        assertL(multiple, PLAYER2);
        assertL(multiple, PLAYER3);

        String layer2 =
                "-----" +
                "-----" +
                "-♥♥--" +
                "--♣--" +
                "-----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);
        assertE(layer2, PLAYER3);

        String forces =
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A00A-=#-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);
        assertF(forces, PLAYER3);

        // when
        tickAll();

        assertE(layer2, PLAYER1);
        assertF(forces, PLAYER1);

        // when
        INCREASE = 2;
        MOVE = 2;
        hero(PLAYER3, 2, 1).right();
        tickAll();

        assertE("-----" +
                "-----" +
                "-♥♥--" +
                "--♣♣-" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A00A-=#-=#\n" +
                "-=#-=#00A002-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // then
        String game1 = single(PLAYER1).getPlayer().getCurrent().id();

        File file = getFile("game-" + game1 + "-1.txt");
        String result = loadFromFile(file);

        String player1 = singles.get(PLAYER1).getPlayer().lg.id();
        String player2 = singles.get(PLAYER2).getPlayer().lg.id();
        String player3 = singles.get(PLAYER3).getPlayer().lg.id();
        String hero1 = hero(PLAYER1).lg.id();
        String hero2 = hero(PLAYER2).lg.id();
        String hero3 = hero(PLAYER3).lg.id();

        assertEquals(("Game started\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "New player P@314c508a registered with hero H@10b48321 with base at '{\"x\":3,\"y\":2}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@314c508a'}\"\n" +
                        "New player P@6b67034 registered with hero H@16267862 with base at '{\"x\":2,\"y\":1}' and color '2' for user 'demo3@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo3@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6b67034'}\"\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@16267862 of player P@6b67034 received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.3.│└───┘\",\"-----------♥-♦---♣-------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":1,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":1,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@16267862 of player P@6b67034 received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.3.│└───┘\",\"-----------♥♥♦---♣-------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":2,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":2,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@16267862 of player P@6b67034 received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.3.│└───┘\",\"-----------♥♥----♣-------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a is not alive\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@16267862 of player P@6b67034 received command:'{}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.3.│└───┘\",\"-----------♥♥----♣-------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a is not alive\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Hero H@16267862 of player P@6b67034 received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":2,\"y\":1}}],\"movements\":[{\"count\":2,\"direction\":\"RIGHT\",\"region\":{\"x\":2,\"y\":1}}]}'\n" +
                        "Board:'{\"layers\":[\"╔═══┐║...│║1.2│║.3.│└───┘\",\"-----------♥♥----♣♣------\",\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A-=#-=#-=#-=#00A002-=#-=#-=#-=#-=#-=#\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n")
                        .replace("P@6c3f5566", player1)
                        .replace("P@314c508a", player2)
                        .replace("P@6b67034", player3)
                        .replace("H@12405818", hero1)
                        .replace("H@10b48321", hero2)
                        .replace("H@16267862", hero3)
                        .replace("E@6a396c1e", game1),
                result);
    }

    @NotNull
    private String loadFromFile(File file) {
        return Levels.loadLines(
                    file.getAbsolutePath(),
                    StringBuffer::new,
                    (container, line) -> container.append(line).append('\n')
            ).toString();
    }

    private File getFile(String fileName) {
        for (File file : gameDataFolder.listFiles()) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        fail("File not found " + fileName);
        return null;
    }

    @Ignore
    @Test
    public void shouldCreateNewFileWhenGoToAnotherGameAfterLobby() {
        // given
        data.gameLoggingEnable(true)
                .lobbyCapacity(4)
                .lobbyEnable(true);

        String single =
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘";
        String multiple =
                "╔══┐" +
                "║12│" +
                "║34│" +
                "└──┘";
        givenFlWithWaitForAllLobby(single, multiple);
        createPlayers(4);

        // when
        // players go to next level
        hero(PLAYER1, 1, 2).right();
        hero(PLAYER2, 1, 2).right();
        hero(PLAYER3, 1, 2).right();
        hero(PLAYER4, 1, 2).right();
        tickAll();

        tickAll();
        // TODO что-то непонятное с этим тестои, либо я перемудрил... Тут не переходим на multiple

        // then
        String layer2 =
                "----" +
                "-♥♦-" +
                "-♣♠-" +
                "----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);
        assertE(layer2, PLAYER3);
        assertE(layer2, PLAYER4);

        String forces =
                "-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);
        assertF(forces, PLAYER3);
        assertF(forces, PLAYER4);

        // when select different way
        INCREASE = 10;
        MOVE = 10;
        hero(PLAYER1, 1, 2).right();
        hero(PLAYER3, 1, 1).right();
        tickAll();

        // then
        layer2 =
                "----" +
                "-♥--" +
                "-♣--" +
                "----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);
        assertE(layer2, PLAYER3);
        assertE(layer2, PLAYER4);

        forces =
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);
        assertF(forces, PLAYER3);
        assertF(forces, PLAYER4);

        // when
        INCREASE = 10;
        MOVE = 10;
        hero(PLAYER1, 1, 2).down();
        tickAll();

        // then
        layer2 =
                "----" +
                "-♥--" +
                "----" +
                "----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);
        assertE(layer2, PLAYER3);
        assertE(layer2, PLAYER4);

        forces =
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);
        assertF(forces, PLAYER3);
        assertF(forces, PLAYER4);

        // when
        // new game
        tickAll();

        layer2 =
                "----" +
                "-♥♦-" +
                "-♣♠-" +
                "----";
        assertE(layer2, PLAYER1);
        assertE(layer2, PLAYER2);
        assertE(layer2, PLAYER3);
        assertE(layer2, PLAYER4);

        forces =
                "-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n";
        assertF(forces, PLAYER1);
        assertF(forces, PLAYER2);
        assertF(forces, PLAYER3);
        assertF(forces, PLAYER4);

        // when
        tickAll();

        // then
        String game1 = single(PLAYER1).getPlayer().getCurrent().id();

        File file1 = getFile("game-" + game1 + "-1.txt");
        String result1 = loadFromFile(file1);

        String player1 = singles.get(PLAYER1).getPlayer().lg.id();
        String player2 = singles.get(PLAYER2).getPlayer().lg.id();
        String hero1 = hero(PLAYER1).lg.id();
        String hero2 = hero(PLAYER2).lg.id();

        assertEquals(("Game started\n" +
                        "New player P@6c3f5566 registered with hero H@12405818 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@6c3f5566'}\"\n" +
                        "New player P@314c508a registered with hero H@10b48321 with base at '{\"x\":3,\"y\":2}' and color '1' for user 'demo2@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@6a396c1e-1','playerName':'P@314c508a'}\"\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥-♦-----------\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":1,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":1,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♥♦-----------\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@12405818 of player P@6c3f5566 received command:'{\"increase\":[{\"count\":10,\"region\":{\"x\":2,\"y\":2}}],\"movements\":[{\"count\":10,\"direction\":\"RIGHT\",\"region\":{\"x\":2,\"y\":2}}]}'\n" +
                        "Hero H@10b48321 of player P@314c508a received command:'{}'\n" +
                        "Board:'{\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♥------------\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Game finished\n")
                        .replace("P@6c3f5566", player1)
                        .replace("P@314c508a", player2)
                        .replace("H@12405818", hero1)
                        .replace("H@10b48321", hero2)
                        .replace("E@6a396c1e", game1),
                result1);

        File file2 = getFile("game-" + game1 + "-2.txt");
        String result2 = loadFromFile(file2);

        assertEquals(("Game started\n" +
                        "New player P@7e07db1f registered with hero H@1189dd52 with base at '{\"x\":1,\"y\":2}' and color '0' for user 'demo1@codenjoy.com'\n" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo1@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@35cabb2a-2','playerName':'P@7e07db1f'}\"\n" +

                        "New player P@36bc55de registered with hero H@564fabc8 with base at '{\"x\":3,\"y\":2}' and color '1'\n for user 'demo2@codenjoy.com\n'" +
                        "// Please run \"http://127.0.0.1:8080/codenjoy-contest/admin31415?player=demo2@codenjoy.com&gameName=expansion&data={'startFromTick':0,'replayName':'game-E@35cabb2a-2','playerName':'P@36bc55de'}\"\n" +

                        "Hero H@1189dd52 of player P@7e07db1f received command:'{}'\n" +
                        "Hero H@564fabc8 of player P@36bc55de received command:'{}'\n" +
                        "Board:'{\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥-♦-----------\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n" +
                        "Hero H@1189dd52 of player P@7e07db1f received command:'{}'\n" +
                        "Hero H@564fabc8 of player P@36bc55de received command:'{\"increase\":[{\"count\":2,\"region\":{\"x\":3,\"y\":2}}],\"movements\":[{\"count\":2,\"direction\":\"LEFT\",\"region\":{\"x\":3,\"y\":2}}]}'\n" +
                        "Board:'{\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A00200A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔═══┐║...│║1.2│║...│└───┘\",\"-----------♥♦♦-----------\"],\"offset\":{\"x\":0,\"y\":0}}'\n" +
                        "--------------------------------------------------------------\n")
                        .replace("P@7e07db1f", player1)
                        .replace("P@36bc55de", player2)
                        .replace("H@1189dd52", hero1)
                        .replace("H@564fabc8", hero2)
                        .replace("E@35cabb2a", game1),
                result2);
    }
}
