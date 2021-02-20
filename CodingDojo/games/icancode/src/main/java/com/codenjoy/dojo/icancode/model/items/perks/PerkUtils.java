package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ElementsMapper;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Dice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PerkUtils {

    public static Optional<Perk> random(Dice dice, boolean contest) {
        List<Elements> all = Elements.perks();
        all.removeAll(defaultFor(contest));
        return random(dice, all.toArray(new Elements[]{}));
    }

    private static Optional<Perk> random(Dice dice, Elements... perks) {
        Elements element = perks[dice.next(perks.length)];
        Perk perk = (Perk)ElementsMapper.get(element);
        return Optional.ofNullable(perk);
    }

    public static boolean isPerk(Elements element) {
        return Elements.perks().contains(element);
    }

    // TODO test me with unit
    public static List<Perk> defaultFor(boolean contest) {
        String data = SettingsWrapper.data.defaultPerks();
        if (!data.contains(",")) {
            return Arrays.asList();
        }

        String[] split = data.split(",");
        String perks = split[contest ? 1 : 0];
        return perks.chars()
                .mapToObj(ch -> Elements.valueOf((char)ch))
                .map(element -> (Perk)ElementsMapper.get(element))
                .collect(toList());
    }
}
