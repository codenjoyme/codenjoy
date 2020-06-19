package com.codenjoy.dojo.bomberman.model.perks;

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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Dice;

import java.util.EnumMap;
import java.util.Map;

/**
 * Holds the settings for all available perks.
 */

public class PerksSettingsWrapper {
    private static final Map<Elements, PerkSettings> settings = new EnumMap<>(Elements.class);
    public static int PERCENTAGE = 100;
    private static int dropRatio = 10;
    private static int pickTimeout = 5;

    private PerksSettingsWrapper() {
    }

    public static void setPerkSettings(Elements perk, int value, int timeout) {
        settings.put(perk, new PerkSettings(value, timeout));
    }

    public static PerkSettings getPerkSettings(Elements perk) {
        if (settings.containsKey(perk)) {
            return settings.get(perk);
        } else {
            return new PerkSettings(0, 0);
        }
    }

    public static void clear() {
        settings.clear();
    }

    public static int getDropRatio() {
        return dropRatio;
    }

    public static void setDropRatio(int dropRatio) {
        PerksSettingsWrapper.dropRatio = dropRatio;
    }

    public static int getPickTimeout() {
        return pickTimeout;
    }

    public static void setPickTimeout(int pickTimeout) {
        PerksSettingsWrapper.pickTimeout = pickTimeout;
    }

    /**
     * In total were are 100 chances.
     * Here we have 2 perks with total perk drop chance = 20.
     * For each perk we have a range of successful drop = 20 / 2 = 10.
     * So we have 2 intervals and want to put them on equal distance of each other = 100 / 2 = 50.
     * In result we get 2 ranges of numbers that will return us perk:
     * 0 >= perk1 < 0 + 10  and  0 + 10 + 50 (60) >= perk2 < (70) 0 + 10 + 50 + 10
     * All other ranges will give us Elements.DESTROYED_WALL.
     * <p>
     * The more perks we have, the more number of ranges, the more narrow each range.
     * E.g. for 4 perks range will be 20/4 = 5,
     * step for next range = 100 / 4 = 25,
     * ranges for perks are: 0..5, 30..35, 60..65, 90..95
     *
     * @deprecated Need to switch to the more optimal algorithm in next releases
     * Theory is here https://www.keithschwarz.com/darts-dice-coins/
     */
    public static Elements nextPerkDrop(Dice dice) {
        int perksTotal = settings.size();

        if (perksTotal != 0) {
            int chanceRange = dropRatio / perksTotal;
            int step = PERCENTAGE / perksTotal;
            int rnd = dice.next(PERCENTAGE);
            int lowerBoundary = 0;
            int upperBoundary = lowerBoundary + chanceRange;

            for (Elements perk : settings.keySet()) {
                if (rnd >= lowerBoundary && rnd < upperBoundary) {
                    return perk;
                }

                lowerBoundary = upperBoundary + step;
                upperBoundary = lowerBoundary + chanceRange;
            }
        }
        return Elements.DESTROYED_WALL;
    }
}
