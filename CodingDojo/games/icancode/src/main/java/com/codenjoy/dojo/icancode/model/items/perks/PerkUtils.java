package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ElementsMapper;
import com.codenjoy.dojo.services.Dice;

import java.util.Optional;

public class PerkUtils {

    public static Optional<Perk> random(Dice dice, Elements... perks) {
        Elements element = perks[dice.next(perks.length)];
        Perk perk = (Perk)ElementsMapper.get(element);
        return Optional.ofNullable(perk);
    }

    public static boolean isPerk(Elements element) {
        return Elements.perks().contains(element);
    }
}
