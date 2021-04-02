package com.codenjoy.dojo.utils;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.GameType;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@UtilityClass
public class Smoke {

    public static final String SOURCE_FOLDER = "src/test/resources/";
    public static final String TARGET_FOLDER = "target/";

    public static void play(int iterations,
                            String fileName,
                            GameType gameRunner,
                            List<Solver> solvers,
                            List<ClientBoard> boards,
                            BiConsumer<Object, Object> assertor)
    {
        play(iterations, fileName, true,
                gameRunner, solvers, boards, assertor);
    }

    public static void play(int iterations,
                            String fileName,
                            boolean printBoardOnly,
                            GameType gameRunner,
                            List<Solver> solvers,
                            List<ClientBoard> boards,
                            BiConsumer<Object, Object> assertor)
    {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = messages::add;
        LocalGameRunner.countIterations = iterations;
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = printBoardOnly;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        // when
        LocalGameRunner.run(gameRunner, solvers, boards);

        // then
        String actual = String.join("\n", messages);
        String expected;
        String expectedFile = SOURCE_FOLDER + fileName;
        if (new File(expectedFile).exists()) {
            expected = load(expectedFile);
            saveToFile(TARGET_FOLDER + fileName, actual);
        } else {
            expected = StringUtils.EMPTY;
            saveToFile(expectedFile, actual);
        }
        TestUtils.assertSmoke(true, assertor, expected, actual);
    }

    public void saveToFile(String path, String data) {
        try {
            File actualFile = new File(path);
            System.out.println("Actual data is here: " + actualFile.getAbsolutePath());
            File folder = actualFile.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Files.writeString(actualFile.toPath(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String load(String file) {
        try {
            return Files.lines(new File(file).toPath())
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }
}
