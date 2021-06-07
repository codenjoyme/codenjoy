package com.codenjoy.dojo.icancode.model.items.perks;

import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEFAULT_PERKS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PerkUtilsTest {

    private Dice dice = mock(Dice.class);
    private GameSettings settings;

    @Before
    public void setUp() {
        settings = new GameSettings();
    }

    @Test
    public void test_isPerk() {
        assertEquals(true, PerkUtils.isPerk(Element.UNLIMITED_FIRE_PERK));
        assertEquals(true, PerkUtils.isPerk(Element.MOVE_BOXES_PERK));
        assertEquals(true, PerkUtils.isPerk(Element.JUMP_PERK));
        assertEquals(true, PerkUtils.isPerk(Element.FIRE_PERK));
        assertEquals(true, PerkUtils.isPerk(Element.DEATH_RAY_PERK));
        assertEquals(true, PerkUtils.isPerk(Element.UNSTOPPABLE_LASER_PERK));

        assertEquals(false, PerkUtils.isPerk(Element.ROBO));
    }

    @Test
    public void test_getDefaultFor_caseSetAll() {
        assertDefaultPerks("ajm,lrf",
                "['a', 'j', 'm']",
                "['l', 'r', 'f']");
    }

    private void assertDefaultPerks(String perks,
                                    String expectedTraining,
                                    String expectedContest)
    {
        settings.string(DEFAULT_PERKS, perks);

        assertEquals(expectedTraining,
                PerkUtils.defaultFor(ICanCode.TRAINING, settings).toString());

        assertEquals(expectedContest,
                PerkUtils.defaultFor(ICanCode.CONTEST, settings).toString());
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

    private void assertRandom(String perks, boolean contest, String expected) {
        settings.string(DEFAULT_PERKS, perks);

        int length = Element.perks().size();

        List<Perk> result = new LinkedList<>();
        for (int index = 0; index < length; index++) {
            when(dice.next(anyInt())).thenReturn(index);

            try {
                Optional<Perk> perk = PerkUtils.random(dice, contest, settings);
                result.add(perk.get());
            } catch (Exception e) {
                // do nothing
            }
        }

        assertEquals(expected, result.toString());
    }
}