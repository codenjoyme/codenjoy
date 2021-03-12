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
import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.services.Dice;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;

public class PerksSettingsWrapper {

    public static final int MAX_PERCENTS = 100;

    private GameSettings settings;

    public PerksSettingsWrapper(GameSettings settings) {
        this.settings = settings;
    }

    public PerksSettingsWrapper put(Elements perk, int value, int timeout) {
        enable(perk);
        switch (perk) {
            case BOMB_BLAST_RADIUS_INCREASE : {
                settings.integer(PERK_BOMB_BLAST_RADIUS_INC, value);
                settings.integer(TIMEOUT_BOMB_BLAST_RADIUS_INC, timeout);
            } break;

            case BOMB_COUNT_INCREASE : {
                settings.integer(PERK_BOMB_COUNT_INC, value);
                settings.integer(TIMEOUT_BOMB_COUNT_INC, timeout);
            } break;

            case BOMB_IMMUNE : {
                // value is always 0
                settings.integer(TIMEOUT_BOMB_IMMUNE, timeout);
            } break;

            case BOMB_REMOTE_CONTROL : {
                settings.integer(REMOTE_CONTROL_COUNT, value);
                // timeout is always 1
            } break;
        }

        return this;
    }

    private void enable(Elements perk) {
        Set<Elements> perks = new LinkedHashSet(enabled());
        perks.add(perk);
        enabled(perks);
    }

    private void enabled(Set<Elements> perks) {
        String chars = perks.stream()
                .map(element -> String.valueOf(element.ch()))
                .reduce("", String::concat);
        settings.string(DEFAULT_PERKS, chars);
    }

    private List<Elements> enabled() {
        return settings.string(DEFAULT_PERKS)
                .chars()
                .mapToObj(ch -> Elements.valueOf((char)ch))
                .collect(toList());
    }

    public PerkSettings get(Elements perk) {
        int value;
        int timeout;
        switch (perk) {
            case BOMB_BLAST_RADIUS_INCREASE : {
                value = settings.integer(PERK_BOMB_BLAST_RADIUS_INC);
                timeout = settings.integer(TIMEOUT_BOMB_BLAST_RADIUS_INC);
            } break;

            case BOMB_COUNT_INCREASE : {
                value = settings.integer(PERK_BOMB_COUNT_INC);
                timeout = settings.integer(TIMEOUT_BOMB_COUNT_INC);
            } break;

            case BOMB_IMMUNE : {
                value = 0;
                timeout = settings.integer(TIMEOUT_BOMB_IMMUNE);
            } break;

            case BOMB_REMOTE_CONTROL : {
                value = settings.integer(REMOTE_CONTROL_COUNT);
                timeout = 1;
            } break;

            default: {
                value = 0;
                timeout = 0;
            } break;
        }

        return new PerkSettings(value, timeout);
    }

    public int dropRatio() {
        return settings.integer(PERK_DROP_RATIO);
    }

    public PerksSettingsWrapper dropRatio(int dropRatio) {
        settings.integer(PERK_DROP_RATIO, dropRatio);
        return this;
    }

    public int pickTimeout() {
        return settings.integer(PERK_PICK_TIMEOUT);
    }

    public PerksSettingsWrapper pickTimeout(int pickTimeout) {
        settings.integer(PERK_PICK_TIMEOUT, pickTimeout);
        return this;
    }

    /**
     * Всего у нас 100 шансов. Кидаем кубик, если он выпадает больше заявленнго dropRatio=20%
     * то рисуется стена. Иначе мы определяем какой индекс перка выпал
     */
    public Elements nextPerkDrop(Dice dice) {
        // нет перков - стенка
        int total = enabled().size();
        if (total == 0) {
            return Elements.DESTROYED_WALL;
        }

        // dropRatio - вероятность выпадения любого перка
        int random = dice.next(MAX_PERCENTS);
        if (random >= dropRatio()) {
            return Elements.DESTROYED_WALL;
        }

        // считаем какой перк победил
        int index = (int)Math.floor(1D * total * random / dropRatio());
        return enabled().get(index);
    }
}
