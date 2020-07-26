package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

public class PrizeChoiceTest {

    private Player player = mock(Player.class);
    private Point pt;

    PrizeChoice prizeChoice = new PrizeChoice();

    @Before
    public void setup() {
        prizeChoice.clear();
        pt = pt(4, 4);
    }

    @Test
    public void shouldAddPrizes() {
        prizeChoice.addPrizes(pt);

        assertEquals(3, prizeChoice.getPrizes().size());
    }

    @Test
    public void shouldGetPrizeImmortality() {
        prizeChoice.addPrizes(pt);
        prizeChoice.setDice(getPrizeChoiceDice(0));

        Prize prizeImmortality = prizeChoice.getPrize();

        assertEquals(Elements.PRIZE_IMMORTALITY, prizeImmortality.state(player, pt));
    }

    @Test
    public void shouldGetPrizeBreakingWalls() {
        prizeChoice.addPrizes(pt);
        prizeChoice.setDice(getPrizeChoiceDice(1));

        Prize prizeBreakingWalls = prizeChoice.getPrize();

        assertEquals(Elements.PRIZE_BREAKING_WALLS, prizeBreakingWalls.state(player, pt));
    }

    @Test
    public void shouldGetPrizeWalkingWater() {
        prizeChoice.addPrizes(pt);
        prizeChoice.setDice(getPrizeChoiceDice(2));

        Prize prizeWalkingWater = prizeChoice.getPrize();

        assertEquals(Elements.PRIZE_WALKING_ON_WATER, prizeWalkingWater.state(player, pt));
    }

    private Dice getPrizeChoiceDice(int value) {
        Dice dice = mock(Dice.class);
        Mockito.when(dice.next(anyInt())).thenReturn(value);
        return dice;
    }
}
