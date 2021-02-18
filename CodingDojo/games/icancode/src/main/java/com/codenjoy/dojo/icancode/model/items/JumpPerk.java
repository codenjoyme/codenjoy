package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Hero;

public class JumpPerk extends AbstractPerk {

    public JumpPerk() {
        super(Elements.JUMP_PERK);
    }

    // For reflection
    public JumpPerk(Elements element) {
        super(element);
    }

    @Override
    protected void activate(Hero hero) {
        hero.setCanJump(true);
    }
}
