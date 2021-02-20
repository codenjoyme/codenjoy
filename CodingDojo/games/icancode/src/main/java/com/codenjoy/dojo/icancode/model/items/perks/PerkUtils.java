package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ElementsMapper;
import com.codenjoy.dojo.services.Dice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PerkUtils {

    public static Optional<Perk> random(Dice dice, Elements... perks) {
        Elements element = perks[dice.next(perks.length)];
        Perk perk = (Perk)ElementsMapper.get(element);
        return Optional.ofNullable(perk);
    }

    public static List<Perk> get(Elements... perks) {
        return Arrays.stream(perks)
                .map(element -> (Perk)ElementsMapper.get(element))
                .collect(toList());
    }

    public static boolean isPerk(Elements element) {
        return Elements.perks().contains(element);
    }
}
