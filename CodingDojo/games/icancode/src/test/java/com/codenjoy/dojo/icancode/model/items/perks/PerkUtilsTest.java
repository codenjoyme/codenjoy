package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PerkUtilsTest {

    private Dice dice = mock(Dice.class);

    @Test
    public void test_isPerk() {
        assertEquals(true, PerkUtils.isPerk(Elements.UNLIMITED_FIRE_PERK));
        assertEquals(true, PerkUtils.isPerk(Elements.MOVE_BOXES_PERK));
        assertEquals(true, PerkUtils.isPerk(Elements.JUMP_PERK));
        assertEquals(true, PerkUtils.isPerk(Elements.FIRE_PERK));
        assertEquals(true, PerkUtils.isPerk(Elements.DEATH_RAY_PERK));
        assertEquals(true, PerkUtils.isPerk(Elements.UNSTOPPABLE_LASER_PERK));

        assertEquals(false, PerkUtils.isPerk(Elements.ROBO));
    }

    @Test
    public void test_getDefaultFor_caseSetAll() {
        assertDefaultPerks("ajm,lrf",
                "['a', 'j', 'm']",
                "['l', 'r', 'f']");
    }

    private void assertDefaultPerks(String settings,
                                    String expectedTraining,
                                    String expectedContest)
    {
        SettingsWrapper.setup().defaultPerks(settings);

        assertEquals(expectedTraining,
                PerkUtils.defaultFor(ICanCode.TRAINING).toString());

        assertEquals(expectedContest,
                PerkUtils.defaultFor(ICanCode.CONTEST).toString());
    }

    @Test
    public void test_getDefaultFor_caseSetOnlyContest() {
        assertDefaultPerks(",lrf",
                "[]",
                "['l', 'r', 'f']");
    }

    @Test
    public void test_getDefaultFor_caseSetOnlyTraining() {
        assertDefaultPerks("ajm,",
                "['a', 'j', 'm']",
                "[]");
    }

    @Test
    public void test_getDefaultFor_caseNotSet1() {
        assertDefaultPerks(",",
                "[]",
                "[]");
    }

    @Test
    public void test_getDefaultFor_caseNotSet2() {
        assertDefaultPerks("",
                "[]",
                "[]");
    }

    @Test
    public void test_random_caseSetAll() {
        assertRandom("ajm,lrf", ICanCode.TRAINING,
                "['l', 'r', 'f']");

        assertRandom("ajm,lrf", ICanCode.CONTEST,
                "['a', 'j', 'm']");
    }

    @Test
    public void test_random_caseSetOnlyContest() {
        assertRandom(",lrf", ICanCode.TRAINING,
                "['l', 'r', 'f', 'a', 'j', 'm']");

        assertRandom(",lrf", ICanCode.CONTEST,
                "['a', 'j', 'm']");
    }

    @Test
    public void test_random_caseSetOnlyTraining() {
        assertRandom("lrf,", ICanCode.TRAINING,
                "['a', 'j', 'm']");

        assertRandom("lrf,", ICanCode.CONTEST,
                "['l', 'r', 'f', 'a', 'j', 'm']");
    }

    @Test
    public void test_random_caseSetOnlyOne() {
        assertRandom("l,r", ICanCode.TRAINING,
                "['r', 'f', 'a', 'j', 'm']");

        assertRandom("l,r", ICanCode.CONTEST,
                "['l', 'f', 'a', 'j', 'm']");
    }

    @Test
    public void test_random_caseNotSet1() {
        assertRandom(",", ICanCode.TRAINING,
                "['l', 'r', 'f', 'a', 'j', 'm']");

        assertRandom(",", ICanCode.CONTEST,
                "['l', 'r', 'f', 'a', 'j', 'm']");
    }

    @Test
    public void test_random_caseNotSet2() {
        assertRandom("", ICanCode.TRAINING,
                "['l', 'r', 'f', 'a', 'j', 'm']");

        assertRandom("", ICanCode.CONTEST,
                "['l', 'r', 'f', 'a', 'j', 'm']");
    }

    private void assertRandom(String settings, boolean contest, String expected) {
        SettingsWrapper.setup().defaultPerks(settings);

        int length = Elements.perks().size();

        List<Perk> result = new LinkedList<>();
        for (int index = 0; index < length; index++) {
            when(dice.next(anyInt())).thenReturn(index);

            try {
                Optional<Perk> perk = PerkUtils.random(dice, contest);
                result.add(perk.get());
            } catch (Exception e) {
                // do nothing
            }
        }

        assertEquals(expected, result.toString());
    }
}