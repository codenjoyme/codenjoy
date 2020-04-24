package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Tickable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HeroPerks implements Tickable {
    private Map<String, Perk> perks = new HashMap<>();

    public void add(Perk perk) {
        if (perks.containsKey(perk.getName())) {
            Perk newPerk = perk.combine(perks.get(perk.getName()));
            perks.put(perk.getName(), newPerk);
        } else {
            perks.put(perk.getName(), perk);
        }
    }

    public Perk getPerk(Elements element) {
        return perks.get(element.name());
    }

    public Collection<Perk> getPerksList() {
        return perks.values();
    }

    @Override
    public void tick() {
        Map<String, Perk> activePerks = new HashMap<>();
        perks.forEach((name, perk) -> {
            perk.tick();
            if (perk.isActive()) {
                activePerks.put(name, perk);
            }
        });

        perks = activePerks;
    }
}
