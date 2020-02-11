package com.codenjoy.dojo.bomberman.client.simple;

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

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.io.*;
import java.util.Arrays;

public class YourSolverLite implements Solver<Board> {

    public static final String MAIN_RULE_FILE_NAME = "/main.rule";

    private Rules rules;
    private File main;
    private Dice dice;
    private Board board;

    public YourSolverLite(File main, Dice dice) {
        this.main = main;
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isMyBombermanDead()) return "";

        rules = new Rules();
        
        RuleReader reader = new RuleReader();
        reader.load(rules, main);
        
        if (reader.hasErrors()) {
            reader.errors().forEach(System.out::println);
        }

        return rules.process(board).toString();
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("[ERROR] " + Messages.BAD_FORMAT_PLEASE_RUN_PROGRAM_WITH_2_ARGUMENTS + ": \n" +
                    "\t\t\t1) board url 'http://codenjoy.com:80/codenjoy-contest/board/player/playerId?code=1234567890123456789'\n" +
                    "\t\t\t2) rules directory 'games/bomberman/rules'.\n" +
                    "\t\tArguments are: " + Arrays.toString(args));
            return;
        }

        File directory = new File(args[1]);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("[ERROR] " + Messages.RULES_DIRECTORY_NOT_FOUND_HERE + 
                    ": " + directory.getAbsolutePath());
            return;
        }
        File mainRuleFile = new File(directory.getAbsolutePath() + "/" + MAIN_RULE_FILE_NAME);
        if (!mainRuleFile.exists() || !mainRuleFile.isFile()) {
            System.out.println("[ERROR] " + Messages.MAIN_RULE_FILE_NOT_FOUND_HERE + 
                    ": " + mainRuleFile.getAbsolutePath());
            return;
        }

        WebSocketRunner.runClient(
                args[0],
                new YourSolverLite(mainRuleFile, new RandomDice()),
                new Board());
    }

}
