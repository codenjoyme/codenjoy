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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Holds the settings for all available perks.
 */

public class PerksSettingsWrapper {

    private static final Map<Elements, PerkSettings> settings = new EnumMap<>(Elements.class);

    private static int percentage;
    private static int dropRatio;
    private static int pickTimeout;

    public static void reset() {
        percentage = 100;
        dropRatio = 10;
        pickTimeout = 5;
    }

    static {
        reset();
    }

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
     * Всего у нас 100 шансов. Кидаем кубик, если он выпадает больше заявленнго dropRatio=20%
     * то рисуется стена. Иначе мы определяем какой индекс перка выпал
     */
    public static Elements nextPerkDrop(Dice dice) {
        // нет перков - стенка
        int total = settings.size();
        if (total == 0) {
            return Elements.DESTROYED_WALL;
        }

        // dropRatio - вероятность выпадения любого перка
        int random = dice.next(percentage);
        if (random >= dropRatio) {
            return Elements.DESTROYED_WALL;
        }

        // считаем какой перк победил
        int index = (int)Math.floor(1D * total * random / dropRatio);
        return new ArrayList<>(settings.keySet()).get(index);
    }
}
