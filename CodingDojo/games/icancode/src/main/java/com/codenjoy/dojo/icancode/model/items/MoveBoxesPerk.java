package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Hero;

public class MoveBoxesPerk extends AbstractPerk {

    public MoveBoxesPerk() {
        super(Elements.MOVE_BOXES_PERK);
    }

    public MoveBoxesPerk(Elements element) {
        super(element);
    }

    @Override
    protected void activate(Hero hero) {
        hero.setCanMoveBoxes(true);
    }
}
