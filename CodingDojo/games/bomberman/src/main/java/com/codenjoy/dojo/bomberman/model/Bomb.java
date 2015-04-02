package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 1:37 PM
 */
public class Bomb extends PointImpl implements Tickable, State<Elements, Player> {
    protected int timer = 5;
    protected int power;
    private Hero owner;
    private Field field;

    public Bomb(Hero owner, int x, int y, int power, Field field) {
        super(x, y);
        this.power = power;
        this.owner = owner;
        this.field = field;
    }

    public void tick() {
        timer--;
        if (timer == 0) {
            boom();
        }
    }

    private void boom() {
        field.removeBomb(this);
    }

    public int getTimer() {
        return timer;
    }

    public int getPower() {
        return power;
    }

    public boolean isExploded() {
        return timer == 0;
    }

    public boolean itsMine(Hero bomberman) {
        return this.owner == bomberman;
    }

    public Hero getOwner() {
        return owner;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        switch (timer) {
            case 1 : return Elements.BOMB_ONE;
            case 2 : return Elements.BOMB_TWO;
            case 3 : return Elements.BOMB_THREE;
            case 4 : return Elements.BOMB_FOUR;
            case 5 : return Elements.BOMB_FIVE;
            default : return Elements.BOOM;
        }
    }
}
