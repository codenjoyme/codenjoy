package com.codenjoy.dojo.services.round;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettingsImpl.ROUNDS;

public interface RoundSettings<T extends SettingsReader> extends SettingsReader<T> {

    String ROUNDS = "[Rounds]";

    public enum Keys implements SettingsReader.Key {

        ROUNDS_ENABLED(ROUNDS + " Enabled"),
        ROUNDS_PLAYERS_PER_ROOM(ROUNDS + " Players per room"),
        ROUNDS_TIME(ROUNDS + " Time per Round"),
        ROUNDS_TIME_FOR_WINNER(ROUNDS + " Time for Winner"),
        ROUNDS_TIME_BEFORE_START(ROUNDS + " Time before start Round"),
        ROUNDS_PER_MATCH(ROUNDS + " Rounds per Match"),
        ROUNDS_MIN_TICKS_FOR_WIN(ROUNDS + " Min ticks for win");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    static boolean isRounds(List<Key> values) {
        return values.contains(ROUNDS_ENABLED);
    }

    static boolean is(Settings settings) {
        if (settings == null) return false;

        return settings instanceof RoundSettings
                || allRoundsKeys().stream()
                        .map(Key::key)
                        .allMatch(settings::hasParameter);
    }

    // TODO AI765 test me
    static RoundSettingsImpl get(Settings settings) {
        if (RoundSettings.is(settings)) {
            return new RoundSettingsImpl(settings);
        }

        return new RoundSettingsImpl(null);
    }

    static List<SettingsReader.Key> allRoundsKeys() {
        return Arrays.asList(Keys.values());
    }

    static Optional<? extends String> keyToName(List<Key> values, String value) {
        return Optional.ofNullable(
                isRounds(values)
                        ? SettingsReader.Key.keyToName(values, value).orElse(null)
                        : null);
    }

    static Optional<? extends String> nameToKey(List<Key> values, String value) {
        return Optional.ofNullable(
                isRounds(values)
                        ? SettingsReader.Key.nameToKey(values, value).orElse(null)
                        : null);
    }

    default void initRound() {
        // включен ли режим раундов
        bool(ROUNDS_ENABLED, true);

        // сколько участников в комнате
        integer(ROUNDS_PLAYERS_PER_ROOM, 5);

        // сколько тиков на 1 раунд
        integer(ROUNDS_TIME, 200);

        // сколько тиков победитель будет сам оставаться после всех побежденных
        integer(ROUNDS_TIME_FOR_WINNER, 1);

        // обратный отсчет перед началом раунда
        integer(ROUNDS_TIME_BEFORE_START, 5);

        // сколько раундов (с тем же составом героев) на 1 матч
        integer(ROUNDS_PER_MATCH, 1);

        // сколько тиков должно пройти от начала раунда, чтобы засчитать победу
        integer(ROUNDS_MIN_TICKS_FOR_WIN, 1);
    }

    // parameters getters

    default Parameter<Boolean> roundsEnabled() {
        return boolValue(Keys.ROUNDS_ENABLED);
    }

    default Parameter<Integer> playersPerRoom() {
        return integerValue(Keys.ROUNDS_PLAYERS_PER_ROOM);
    }

    default Parameter<Integer> timePerRound() {
        return integerValue(ROUNDS_TIME);
    }

    default Parameter<Integer> timeForWinner() {
        return integerValue(Keys.ROUNDS_TIME_FOR_WINNER);
    }

    default Parameter<Integer> timeBeforeStart() {
        return integerValue(Keys.ROUNDS_TIME_BEFORE_START);
    }

    default Parameter<Integer> roundsPerMatch() {
        return integerValue(Keys.ROUNDS_PER_MATCH);
    }

    default Parameter<Integer> minTicksForWin() {
        return integerValue(Keys.ROUNDS_MIN_TICKS_FOR_WIN);
    }

    // update methods

    // TODO test me
    default List<Parameter> getRoundParams() {
        if (getParameters().isEmpty()) {
            return Arrays.asList();
        }
        return new LinkedList<>(){{
            add(roundsEnabled());
            add(playersPerRoom());
            add(timePerRound());
            add(timeForWinner());
            add(timeBeforeStart());
            add(roundsPerMatch());
            add(minTicksForWin());
        }};
    }

    default RoundSettings update(RoundSettings input) {
        setRoundsEnabled(input.isRoundsEnabled());
        setPlayersPerRoom(input.getPlayersPerRoom());
        setTimePerRound(input.getTimePerRound());
        setTimeForWinner(input.getTimeForWinner());
        setTimeBeforeStart(input.getTimeBeforeStart());
        setRoundsPerMatch(input.getRoundsPerMatch());
        setMinTicksForWin(input.getMinTicksForWin());
        return this;
    }

    // TODO AI765 test me
    default RoundSettings updateRound(Settings input) {
        if (input != null) {
            allRoundsKeys().stream()
                    .map(Key::key)
                    .forEach(key -> getParameter(key).update(input.getParameter(key).getValue()));
        }
        return this;
    }

    // getters

    default boolean isRoundsEnabled() {
        return roundsEnabled().getValue();
    }

    default int getPlayersPerRoom() {
        return playersPerRoom().getValue();
    }

    default int getTimePerRound() {
        return timePerRound().getValue();
    }

    default int getTimeForWinner() {
        return timeForWinner().getValue();
    }

    default int getTimeBeforeStart() {
        return timeBeforeStart().getValue();
    }

    default int getRoundsPerMatch() {
        return roundsPerMatch().getValue();
    }

    default int getMinTicksForWin() {
        return minTicksForWin().getValue();
    }

    // setters

    default RoundSettings setRoundsEnabled(boolean input) {
        roundsEnabled().update(input);
        return this;
    }

    default RoundSettings setPlayersPerRoom(int input) {
        playersPerRoom().update(input);
        return this;
    }

    default RoundSettings setTimePerRound(int input) {
        timePerRound().update(input);
        return this;
    }

    default RoundSettings setTimeForWinner(int input) {
        timeForWinner().update(input);
        return this;
    }

    default RoundSettings setTimeBeforeStart(int input) {
        timeBeforeStart().update(input);
        return this;
    }

    default RoundSettings setRoundsPerMatch(int input) {
        roundsPerMatch().update(input);
        return this;
    }

    default RoundSettings setMinTicksForWin(int input) {
        minTicksForWin().update(input);
        return this;
    }
}