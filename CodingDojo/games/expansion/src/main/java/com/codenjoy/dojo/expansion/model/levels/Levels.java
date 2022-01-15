package com.codenjoy.dojo.expansion.model.levels;

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


import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.services.BoardMap;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class Levels {

    private static final Logger log = LoggerFactory.getLogger(Levels.class);

    public static final int COUNT_LAYERS = 3;

    private static Map<String, String> levels = new LinkedHashMap<>();

    public static final String DEMO = make("DEMO");
    public static final String BIG_MULTI1 = make("BIG_MULTI1");
    public static final List<String> MULTI = makeAll("MULTI%s");
    public static final List<String> SINGLE = makeAll("SINGLE%s");

    private static String make(String name) {
        String level = loadFromFile(name);
        if (StringUtils.isEmpty(level)) {
            return null;
        }
        level = level.replace('-', ' ');
        String result = decorate(level);
        put(name, result);
        return name;
    }

    private static List<String> makeAll(String namePattern) {
        List<String> result = new LinkedList<>();

        int index = 0;
        while (true) {
            String fileName = String.format(namePattern, ++index);
            String levelName = make(fileName);
            if (levelName == null) {
                break;
            }
            result.add(levelName);
        }

        return result;
    }

    public static void put(String name, String result) {
        levels.put(name, result);
    }

    public static String get(String name) {
        return levels.get(name);
    }

    public static String loadFromFile(String name) {
        StringBuilder buffer = loadLines(
                "expansion/levels/" + name + ".lev",
                StringBuilder::new,
                StringBuilder::append
        );
        return buffer.toString();
    }

    public static <T> T loadLines(String filePath,
                                Supplier<T> supplier,
                                BiFunction<T, String, T> applier)
    {
        T result = supplier.get();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(open(filePath), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                applier.apply(result, line);
            }
        } catch (NullPointerException e) {
            // file not found
            return result;
        } catch (IOException e) {
            log.error("Error during loading file {}", filePath, e);
        }
        return result;
    }

    private static InputStream open(String filePath) throws IOException {
        ClassLoader classLoader = Levels.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(filePath);
        if (stream != null) {
            return stream;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return Files.newInputStream(file.toPath());
    }

    public static LevelsFactory collectLevels(int boardSize, List<String> levels) {
        return () -> collect(boardSize, levels);
    }

    public static LevelsFactory none() {
        return () -> Arrays.asList();
    }

    public static List<Supplier<Level>> collectYours(final int viewSize, final String... boards) {
        return Arrays.stream(boards)
                .map(board -> {
                    Supplier<Level> result = () -> new Level("level_" + Integer.toHexString(board.hashCode()),
                                    board, viewSize);
                    return result;
                })
                .collect(toList());
    }

    private static List<Level> collect(int viewSize, List<String> names) {
        List<Level> result = new LinkedList<>();
        for (String name : names) {
            result.add(getLevel(viewSize, name));
        }
        return result;
    }

    @NotNull
    public static Level getLevel(int viewSize, String name) {
        String level = get(name);
        if (level == null) {
            level = get(make(name));
        }
        String resize = resize(level, viewSize);
        return new Level(name, resize, viewSize);
    }

    static String resize(String level, int toSize) {
        double sqrt = Math.sqrt(level.length());
        int currentSize = (int) sqrt;
        if (sqrt - currentSize != 0) {
            throw new IllegalArgumentException("Level is not square: " + level);
        }
        if (currentSize >= toSize) {
            return level;
        }

        int before = (toSize - currentSize)/2;
        int after = (toSize - currentSize - before);
        String result = "";
        for (int i = 0; i < currentSize; i++) {
            String part = level.substring(i*currentSize, (i + 1)*currentSize);
            part = StringUtils.leftPad(part, before + part.length());
            part = StringUtils.rightPad(part, after + part.length());
            result += part;
        }
        result = StringUtils.leftPad(result, before*toSize + result.length());
        result = StringUtils.rightPad(result, after*toSize + result.length());

        return result;
    }

    public static String decorate(String level) {
        BoardMap map = new BoardMap(level);
        BoardMap out = new BoardMap(map.size());
        for (int x = 0; x < map.size(); ++x) {
            for (int y = 0; y < map.size(); ++y) {
                char at = map.getAt(x, y);
                if (at != '#') {
                    out.setAt(x, y, at);
                    continue;
                }

                if (chk("###" +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("## " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("###" +
                        "#  " +
                        "   ", x, y, map) ||
                    chk("## " +
                        "#  " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_IN_LEFT.ch());
                } else
                if (chk("###" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk(" ##" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("###" +
                        "  #" +
                        "   ", x, y, map) ||
                    chk(" ##" +
                        "  #" +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_IN_RIGHT.ch());
                } else
                if (chk("#  " +
                        "#  " +
                        "###", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "###", x, y, map) ||
                    chk("#  " +
                        "#  " +
                        "## ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "## ", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_BACK_LEFT.ch());
                } else
                if (chk("  #" +
                        "  #" +
                        "###", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "###", x, y, map) ||
                    chk("  #" +
                        "  #" +
                        " ##", x, y, map) ||
                    chk("   " +
                        "  #" +
                        " ##", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_BACK_RIGHT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "###", x, y, map) ||
                    chk("   " +
                        "   " +
                        " ##", x, y, map) ||
                    chk("   " +
                        "   " +
                        "## ", x, y, map) ||
                    chk("   " +
                        "   " +
                        " # ", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_BACK.ch());
                } else
                if (chk("#  " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("#  " +
                        "#  " +
                        "   ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_LEFT.ch());
                } else
                if (chk("  #" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("  #" +
                        "  #" +
                        "   ", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_RIGHT.ch());
                } else
                if (chk("###" +
                        "   " +
                        "   ", x, y, map) ||
                    chk(" ##" +
                        "   " +
                        "   ", x, y, map) ||
                    chk("## " +
                        "   " +
                        "   ", x, y, map) ||
                    chk(" # " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_FRONT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "  #", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_BACK_ANGLE_LEFT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "#  ", x, y, map))
                {
                    out.setAt(x, y, Element.WALL_BACK_ANGLE_RIGHT.ch());
                } else
                if (chk("#  " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_OUT_RIGHT.ch());
                } else
                if (chk("  #" +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.ANGLE_OUT_LEFT.ch());
                }
                if (chk("   " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Element.BREAK.ch());
                }

            }
        }

        return out.map();
    }

    private static boolean chk(String mask, int x, int y, BoardMap map) {
        BoardMap mm = new BoardMap(mask);
        BoardMap out = new BoardMap(mm.size());
        for (int xx = -1; xx <= 1; xx++) {
            for (int yy = -1; yy <= 1; yy++) {
                char ch = ' ';
                if (map.isOutOf(x + xx, y + yy) || map.getAt(x + xx, y + yy) == ' ') {
                    ch = '#';
                }
                out.setAt(xx + 1, yy + 1, ch);
            }
        }
        String actual = TestUtils.injectN(out.map());
        String expected = TestUtils.injectN(mask);
        return actual.equals(expected);
    }
}