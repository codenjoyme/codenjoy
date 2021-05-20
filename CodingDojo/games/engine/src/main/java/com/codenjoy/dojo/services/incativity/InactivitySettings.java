package com.codenjoy.dojo.services.incativity;

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

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.incativity.InactivitySettings.Keys.*;
import static com.codenjoy.dojo.services.incativity.InactivitySettingsImpl.INACTIVITY;

public interface InactivitySettings<T extends SettingsReader> extends SettingsReader<T> {

    public enum Keys implements SettingsReader.Key {

        INACTIVITY_KICK_PLAYERS(INACTIVITY + " Kick inactive players"),
        INACTIVITY_TIMEOUT(INACTIVITY + " Inactivity timeout ticks");

        private final String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    static boolean isInactivity(List<Key> values) {
        return values.contains(INACTIVITY_TIMEOUT);
    }

    static boolean is(Settings settings) {
        return settings instanceof InactivitySettings
                || allInactivityKeys().stream()
                        .map(Key::key)
                        .allMatch(settings::hasParameter);
    }

    static InactivitySettings get(Settings settings) {
        return new InactivitySettingsImpl(settings);
    }

    static List<SettingsReader.Key> allInactivityKeys() {
        return Arrays.asList(Keys.values());
    }

    static Optional<? extends String> keyToName(List<Key> values, String value) {
        return Optional.ofNullable(
                isInactivity(values)
                        ? SettingsReader.Key.keyToName(values, value).orElse(null)
                        : null);
    }

    static Optional<? extends String> nameToKey(List<Key> values, String value) {
        return Optional.ofNullable(
                isInactivity(values)
                        ? SettingsReader.Key.nameToKey(values, value).orElse(null)
                        : null);
    }

    default void initInactivity() {
        bool(INACTIVITY_KICK_PLAYERS, false);
        integer(INACTIVITY_TIMEOUT, 5);
    }

    // parameters getters

    default Parameter<Boolean> kickInactivePlayers() {
        return boolValue(INACTIVITY_KICK_PLAYERS);
    }

    default Parameter<Integer> inactivityTimeout() {
        return integerValue(INACTIVITY_TIMEOUT);
    }

    // update methods

    // TODO test me
    default List<Parameter> getInactivityParams() {
        if (getParameters().isEmpty()) {
            return Arrays.asList();
        }
        return new LinkedList<>(){{
            add(kickInactivePlayers());
            add(inactivityTimeout());
        }};
    }

    default InactivitySettings update(InactivitySettings input) {
        setKickInactivePlayers(input.isKickInactivePlayers());
        setInactivityTimeout(input.getInactivityTimeout());
        return this;
    }

    // TODO test me
    default InactivitySettings updateInactivity(Settings input) {
        allInactivityKeys().stream()
                .map(Key::key)
                .forEach(key -> getParameter(key).update(input.getParameter(key).getValue()));
        return this;
    }

    // getters

    default boolean isKickInactivePlayers() {
        return kickInactivePlayers().getValue();
    }

    default int getInactivityTimeout() {
        return inactivityTimeout().getValue();
    }

    // setters

    default InactivitySettings setKickInactivePlayers(boolean input) {
        kickInactivePlayers().update(input);
        return this;
    }

    default InactivitySettings setInactivityTimeout(int input) {
        inactivityTimeout().update(input);
        return this;
    }
}
