package com.codenjoy.dojo.icancode.model.items.perks;

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

import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.model.ElementMapper;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEFAULT_PERKS;
import static java.util.stream.Collectors.toList;

public class PerkUtils {

    public static Optional<Perk> random(Dice dice, boolean contest, GameSettings settings) {
        List<Element> all = new LinkedList<>(Element.perks());
        defaultFor(contest, settings).forEach(perk -> all.remove(perk.getState()));
        return random(dice, settings, all.toArray(new Element[]{}));
    }

    private static Optional<Perk> random(Dice dice, GameSettings settings, Element... perks) {
        int index = dice.next(perks.length);
        Element element = perks[index];
        Perk perk = (Perk) ElementMapper.get(element);
        perk.init(settings);
        return Optional.ofNullable(perk);
    }

    public static boolean isPerk(Element element) {
        return Element.perks().contains(element);
    }

    public static List<Perk> defaultFor(boolean contest, GameSettings settings) {
        String data = settings.string(DEFAULT_PERKS);
        if (!data.contains(",")) {
            return Arrays.asList();
        }

        String[] split = data.split(",", -1);
        if (split.length != 2) {
            return Arrays.asList();
        }
        String perks = split[contest ? 1 : 0];
        if (StringUtils.isEmpty(perks)) {
            return Arrays.asList();
        }
        return perks.chars()
                .mapToObj(ch -> Element.valueOf((char)ch))
                .map(element -> (Perk) ElementMapper.get(element))
                .collect(toList());
    }
}
