package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.bomberman.model.Elements.BOOM;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 15:46
 */
public class Blast extends PointImpl implements State<Elements, Player> {

    private Bomberman bomberman;

    public Blast(int x, int y, Bomberman bomberman) {
        super(x, y);
        this.bomberman = bomberman;
    }

    public boolean itsMine(Bomberman bomberman) {
        return this.bomberman == bomberman;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Bomberman bomberman = null;
        Bomb bomb = null;
        MeatChopper meatChopper = null;
        DestroyWall destroyWall = null;

        if (alsoAtPoint[1] instanceof Bomberman) {
            bomberman = (Bomberman)alsoAtPoint[1];
        } else if (alsoAtPoint[1] instanceof Bomb){
            bomb = (Bomb)alsoAtPoint[1];
        } else if (alsoAtPoint[1] instanceof MeatChopper) {
            meatChopper = (MeatChopper)alsoAtPoint[1];
        } else if (alsoAtPoint[1] instanceof DestroyWall) {
            destroyWall = (DestroyWall) alsoAtPoint[1];
        }

        if (bomberman != null) {
            if (bomberman == player.getBomberman()) {
                return DEAD_BOMBERMAN;
            } else {
                return OTHER_DEAD_BOMBERMAN;
            }
        } else if (meatChopper != null) {
            return DEAD_MEAT_CHOPPER;
        } else if (destroyWall != null) {
            return DESTROYED_WALL;
        } else if (bomb == null) {
            return BOOM;
        } else {
            return null;
        }
    }
}
