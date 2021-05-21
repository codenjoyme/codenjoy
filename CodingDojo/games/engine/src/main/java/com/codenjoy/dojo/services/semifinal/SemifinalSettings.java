package com.codenjoy.dojo.services.semifinal;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.semifinal.SemifinalSettings.Keys.*;

public interface SemifinalSettings<T extends SettingsReader> extends SettingsReader<T> {

    String SEMIFINAL = "[Semifinal]";

    public enum Keys implements Key {
        
        SEMIFINAL_ENABLED(SEMIFINAL + " Enabled"),
        SEMIFINAL_TIMEOUT(SEMIFINAL + " Timeout"),
        SEMIFINAL_PERCENTAGE(SEMIFINAL + " Percentage"),
        SEMIFINAL_LIMIT(SEMIFINAL + " Limit"),
        SEMIFINAL_RESET_BOARD(SEMIFINAL + " Reset board"),
        SEMIFINAL_SHUFFLE_BOARD(SEMIFINAL + " Shuffle board");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    static boolean isSemifinal(List<Key> values) {
        return values.contains(SEMIFINAL_ENABLED);
    }

    static boolean is(Settings settings) {
        if (settings == null) return false;

        return settings instanceof SemifinalSettings
                || allSemifinalKeys().stream()
                        .map(Key::key)
                        .allMatch(settings::hasParameter);
    }

    // TODO AI765 test me
    static SemifinalSettingsImpl get(Settings settings) {
        if (SemifinalSettings.is(settings)) {
            return new SemifinalSettingsImpl(settings);
        }

        return new SemifinalSettingsImpl(null);
    }

    static List<SettingsReader.Key> allSemifinalKeys() {
        return Arrays.asList(Keys.values());
    }

    static Optional<? extends String> keyToName(List<Key> values, String value) {
        return Optional.ofNullable(
                isSemifinal(values)
                        ? SettingsReader.Key.keyToName(values, value).orElse(null)
                        : null);
    }

    static Optional<? extends String> nameToKey(List<Key> values, String value) {
        return Optional.ofNullable(
                isSemifinal(values)
                        ? SettingsReader.Key.nameToKey(values, value).orElse(null)
                        : null);
    }

    default void initSemifinal() {
        // включен ли режим полуфиналов
        bool(SEMIFINAL_ENABLED, false);

        // сколько тиков должно пройти, прежде чем произойдет пересчет финалистов
        integer(SEMIFINAL_TIMEOUT, 900);

        // во время пересчета в SEMIFINAL_LIMIT указаны проценты?
        bool(SEMIFINAL_PERCENTAGE, true);

        // если SEMIFINAL_PERCENTAGE = false указывает на
        // количество лидеров вошедших в финал
        // (каждый раз отсекается SEMIFINAL_LIMIT участников)

        // если SEMIFINAL_PERCENTAGE = true указывает на
        // процент лидеров вошедших в следующий раунд полуфинала
        // (каждый раз отсекается % участников, что рано или поздно
        // приведет к 1 лидеру)
        integer(SEMIFINAL_LIMIT, 50);

        // рисетить ли борды после каждого пересчета, чтобы обновить на ней контент
        bool(SEMIFINAL_RESET_BOARD, true);

        // перемешивать ли игроков в комнатах после каждого пересчета
        bool(SEMIFINAL_SHUFFLE_BOARD, true);
    }

    // parameters getters

    default Parameter<Boolean> isEnabledValue() {
        return boolValue(SEMIFINAL_ENABLED);
    }

    default Parameter<Integer> getTimeoutValue() {
        return integerValue(SEMIFINAL_TIMEOUT);
    }

    default Parameter<Boolean> isPercentageValue() {
        return boolValue(SEMIFINAL_PERCENTAGE);
    }

    default Parameter<Integer> getLimitValue() {
        return integerValue(SEMIFINAL_LIMIT);
    }

    default Parameter<Boolean> isResetBoardValue() {
        return boolValue(SEMIFINAL_RESET_BOARD);
    }

    default Parameter<Boolean> isShuffleBoardValue() {
        return boolValue(SEMIFINAL_SHUFFLE_BOARD);
    }

    // update methods

    // TODO test me
    default List<Parameter> getSemifinalParams() {
        if (getParameters().isEmpty()) {
            return Arrays.asList();
        }
        return new LinkedList<>(){{
            add(isEnabledValue());
            add(getTimeoutValue());
            add(isPercentageValue());
            add(getLimitValue());
            add(isResetBoardValue());
            add(isShuffleBoardValue());
        }};
    }

    default SemifinalSettings update(SemifinalSettings input) {
        setEnabled(input.isEnabled());
        setPercentage(input.isPercentage());
        setLimit(input.getLimit());
        setTimeout(input.getTimeout());
        setResetBoard(input.isResetBoard());
        setShuffleBoard(input.isShuffleBoard());
        return this;
    }

    // TODO AI765 test me
    default SemifinalSettings updateSemifinal(Settings input) {
        if (input != null) {
            allSemifinalKeys().stream()
                    .map(Key::key)
                    .forEach(key -> getParameter(key).update(input.getParameter(key).getValue()));
        }
        return this;
    }

    // getters

    default boolean isEnabled() {
        return isEnabledValue().getValue();
    }

    default int getTimeout() {
        return getTimeoutValue().getValue();
    }

    default boolean isPercentage() {
        return isPercentageValue().getValue();
    }

    default int getLimit() {
        return getLimitValue().getValue();
    }

    default boolean isResetBoard() {
        return isResetBoardValue().getValue();
    }

    default boolean isShuffleBoard() {
        return isShuffleBoardValue().getValue();
    }

    // setters

    default SemifinalSettings setEnabled(boolean input) {
        isEnabledValue().update(input);
        return this;
    }

    default SemifinalSettings setTimeout(int input) {
        getTimeoutValue().update(input);
        return this;
    }

    default SemifinalSettings setPercentage(boolean input) {
        isPercentageValue().update(input);
        return this;
    }

    default SemifinalSettings setLimit(int input) {
        getLimitValue().update(input);
        return this;
    }

    default SemifinalSettings setResetBoard(boolean input) {
        isResetBoardValue().update(input);
        return this;
    }

    default SemifinalSettings setShuffleBoard(boolean input) {
        isShuffleBoardValue().update(input);
        return this;
    }
}