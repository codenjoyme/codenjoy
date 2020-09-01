package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.*;

public class PrizeChoice {

    private List<Prize> prizes = new LinkedList<>();
    private PrizeFactory prizeFactory = new PrizeFactoryImpl();
    private Dice dice = new RandomDice();

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void clear() {
        prizes.clear();
    }

    public void addPrizes(Point pt) {
        prizes.add(prizeFactory.getImmortality(pt));
        prizes.add(prizeFactory.getBreakingWalls(pt));
        prizes.add(prizeFactory.getWalkingWater(pt));
    }

    public Prize getPrize() {
        int total = prizes.size();
        return prizes.get(dice.next(total));
    }
}
