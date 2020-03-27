//package com.codenjoy.dojo.tetris.model;
//
///*-
// * #%L
// * Codenjoy - it's a dojo-like platform from developers to developers.
// * %%
// * Copyright (C) 2016 Codenjoy
// * %%
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public
// * License along with this program.  If not, see
// * <http://www.gnu.org/licenses/gpl-3.0.html>.
// * #L%
// */
//
//
//import com.codenjoy.dojo.services.*;
//import com.codenjoy.dojo.tetris.services.TetrisRunner;
//import com.codenjoy.dojo.utils.TestUtils;
//import com.codenjoy.dojo.tetris.services.Events;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mockito.stubbing.OngoingStubbing;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
///**
// * User: sanja
// * Date: 17.12.13
// * Time: 4:47
// */
// TODO доделать этот тест
//public class TetrisRunnerTest {
//
//    private Hero hero;
//    private Dice dice;
//    private EventListener listener;
//    private Player player;
//    private PrinterFactory printer = new PrinterFactoryImpl();
//    private Game game;
//
//    @Before
//    public void setup() {
//        TetrisRunner runner = new TetrisRunner();
//        game = runner.newGame(runner.getPlayerScores(0), printer, "");
//    }
//
//    @Test
//    public void test(){
//        Joystick joystick = game.getJoystick();
//        for (int i = 0; i < 50; i++) {
//            String boardAsString = game.getBoardAsString();
//            System.out.println("boardAsString = " + boardAsString);
//            joystick.act();
//            joystick.down();
//            game.tick();
//        }
//    }
//
//}
