package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.services.Dice;

import java.util.List;
import java.util.Optional;

public class PerkUtils {

    public static Optional<AbstractPerk> random(Dice dice) {
        List<Elements> perks = Elements.perks();
        Elements element = perks.get(dice.next(perks.size()));
        switch (element) {
            case UNSTOPPABLE_LASER_PERK:
                return Optional.of(new UnstoppableLaserPerk(element));
            case DEATH_RAY_PERK:
                return Optional.of(new DeathRayPerk(element));
            case UNLIMITED_FIRE_PERK:
                return Optional.of(new UnlimitedFirePerk(element));
            default:
                return Optional.empty();
        }
    }

    public static boolean isPerk(Elements element) {
        return Elements.perks().contains(element);
    }
}
