package com.codenjoy.dojo.snake.battle.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;

/**
 * @author K.ilya
 *         Date: 05.12.2016
 *         Time: 18:56
 */
public class SnakeHeroData implements HeroData {

    private Hero hero;

    SnakeHeroData(Hero hero) {
        this.hero = hero;
    }

    @Override
    public Point getCoordinate() {
        return hero.getHead();
    }

    @Override
    public boolean isSingleBoardGame() {
        return false;
    }

    @Override
    public Object getAdditionalData() {
        return null;
    }
}
