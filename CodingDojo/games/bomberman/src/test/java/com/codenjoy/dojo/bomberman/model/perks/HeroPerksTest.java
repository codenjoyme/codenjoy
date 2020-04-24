package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class HeroPerksTest {

    @Test
    public void should_reset_timer_when_add_Bomb_Blast_Increase_Twice() {
        HeroPerks hp = new HeroPerks();
        BombBlastIncrease bip = new BombBlastIncrease(2, 5);
        Elements e = Elements.BOMB_BLAST_RADIUS_INCREASE;

        hp.add(bip);
        hp.tick();
        hp.tick();

        Perk oldBip = hp.getPerk(e);
        assertEquals("Perk timer is expected to be countdown", 3, oldBip.getTimer());

        hp.add(bip);
        Perk newBip = hp.getPerk(e);
        assertEquals("Perk timer is expected to be reset", 5, newBip.getTimer());

    }

    @Test
    public void should_remove_Perk_on_expiration() {
        HeroPerks hp = new HeroPerks();
        BombBlastIncrease bip = new BombBlastIncrease(2, 2);
        hp.add(bip);

        hp.tick();
        int perksCount = hp.getPerksList().size();
        assertEquals("Perks list must not be empty", 1, perksCount);

        hp.tick();
        perksCount = hp.getPerksList().size();
        assertEquals("Perks list must be empty", 0, perksCount);
    }

}