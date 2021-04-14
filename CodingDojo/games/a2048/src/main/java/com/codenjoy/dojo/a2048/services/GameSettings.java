package com.codenjoy.dojo.a2048.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.a2048.model.Level;
import com.codenjoy.dojo.a2048.model.LevelImpl;
import com.codenjoy.dojo.a2048.model.generator.CornerGenerator;
import com.codenjoy.dojo.a2048.model.generator.Generator;
import com.codenjoy.dojo.a2048.model.generator.RandomGenerator;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.RequestLog;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.codenjoy.dojo.a2048.services.GameSettings.BreaksMode.*;
import static com.codenjoy.dojo.a2048.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum BreaksMode implements SettingsReader.Key {

        BREAKS_EXISTS("With breaks mode"),
        BREAKS_NOT_EXISTS("Without breaks mode");

        private String key;

        BreaksMode(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public enum NumbersMode implements SettingsReader.Key {

        NEW_NUMBERS_IN_CORNERS("Classic (corner only) mode"),
        NEW_NUMBERS_IN_RANDOM("With random numbers mode");

        private String key;

        NumbersMode(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public enum Keys implements SettingsReader.Key {

        SIZE("Size"),
        NEW_NUMBERS("New numbers"),
        NUMBERS_MODE("Numbers mode"),
        BREAKS_MODE("Breaks mode"),
        LEVEL_MAP("Level map");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Stream.of(Arrays.stream(Keys.values()),
                Arrays.stream(NumbersMode.values()),
                Arrays.stream(BreaksMode.values()))
                .flatMap(stream -> stream)
                .collect(toList());
    }

    public GameSettings() {
        multiline(LEVEL_MAP, null).onChange(updateSize());

        integer(SIZE, 5).integerValue(SIZE).onChange(rebuildMap());

        integer(NEW_NUMBERS, 4);

        options(NUMBERS_MODE,
                Arrays.asList(NumbersMode.NEW_NUMBERS_IN_CORNERS.key(), NumbersMode.NEW_NUMBERS_IN_RANDOM.key()),
                NumbersMode.NEW_NUMBERS_IN_CORNERS.key());

        options(BREAKS_MODE,
                Arrays.asList(BREAKS_EXISTS.key(), BREAKS_NOT_EXISTS.key()),
                BREAKS_NOT_EXISTS.key())
                .onChange(rebuildMap());

        rebuildMap().accept(null);
    }

    private Consumer<String> updateSize() {
        // TODO вот тут конечно не очень хорошо
        return map -> parameter(SIZE, EditBox.class)
                .justSet((int)Math.sqrt(map.length()));
    }

    private Consumer<Integer> rebuildMap() {
        return value -> string(LEVEL_MAP, buildMap(integer(SIZE)));
    }

    private String buildMap(int size) {
        String line = StringUtils.leftPad("", size, ' ') + '\n';
        String noBreaks = line.repeat(size);
        if (breaks() == BREAKS_NOT_EXISTS) {
            return noBreaks;
        }

        switch (size) {
            case 3 : return
                    "   \n" +
                    " x \n" +
                    "   \n";
            case 4 : return
                    "    \n" +
                    " x  \n" +
                    "  x \n" +
                    "    \n";
            case 5 : return
                    "  x  \n" +
                    "     \n" +
                    "x   x\n" +
                    "     \n" +
                    "  x  \n";
            case 6 : return
                    "  xx  \n" +
                    "      \n" +
                    "x    x\n" +
                    "x    x\n" +
                    "      \n" +
                    "  xx  \n";
            case 7 : return
                    "  xxx  \n" +
                    "   x   \n" +
                    "x     x\n" +
                    "xx   xx\n" +
                    "x     x\n" +
                    "   x   \n" +
                    "  xxx  \n";
            case 8 : return
                    "   xx   \n" +
                    "   xx   \n" +
                    "        \n" +
                    "xx    xx\n" +
                    "xx    xx\n" +
                    "        \n" +
                    "   xx   \n" +
                    "   xx   \n";
            case 9 : return
                    "   xxx   \n" +
                    "   x x   \n" +
                    "         \n" +
                    "xx     xx\n" +
                    "x       x\n" +
                    "xx     xx\n" +
                    "         \n" +
                    "   x x   \n" +
                    "   xxx   \n";
            default: return noBreaks;
        }
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

    public BreaksMode breaks() {
        return BreaksMode.values()[parameter(BREAKS_MODE, SelectBox.class).index()];
    }

    public NumbersMode numbers() {
        return NumbersMode.values()[parameter(NUMBERS_MODE, SelectBox.class).index()];
    }

    public Generator generator(Dice dice) {
        return generator().apply(dice, integer(NEW_NUMBERS));
    }

    private BiFunction<Dice, Integer, Generator> generator() {
        switch (numbers()) {
            case NEW_NUMBERS_IN_RANDOM:
                return RandomGenerator::new;
            case NEW_NUMBERS_IN_CORNERS:
            default:
                return CornerGenerator::new;
        }

    }

}
