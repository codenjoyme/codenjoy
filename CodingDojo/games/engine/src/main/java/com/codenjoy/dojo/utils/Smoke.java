package com.codenjoy.dojo.utils;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.GameType;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@UtilityClass
public class Smoke {

    public static void play(int iterations,
                            GameType gameRunner,
                            List<Solver> solvers,
                            List<ClientBoard> boards,
                            BiConsumer<Object, Object> assertor)
    {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = message -> {
            messages.add(message);
        };
        LocalGameRunner.countIterations = iterations;
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        // when
        LocalGameRunner.run(gameRunner, solvers, boards);

        // then
        String expectedAll = load("src/test/resources/SmokeTest.data");
        String actualAll = String.join("\n", messages);

        saveToFile("target/ActualSmokeTest.data", actualAll);

        TestUtils.assertSmoke(true, assertor, expectedAll, actualAll);
    }

    public void saveToFile(String path, String data) {
        try {
            File actualFile = new File(path + "_" + Math.abs(new Random().nextInt(Integer.MAX_VALUE)));
            System.out.println("Actual data is here: " + actualFile.getAbsolutePath());
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
