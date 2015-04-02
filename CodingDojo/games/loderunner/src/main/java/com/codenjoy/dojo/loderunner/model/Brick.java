package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:06
 */
public class Brick extends PointImpl implements Tickable, State<Elements, Player> {

    public static final int DRILL_TIMER = 13;
    public static final Brick NULL = new Brick(new PointImpl(-1, -1));
    private int drill;

    private Hero drilledBy;

    public Brick(Point xy) {
        super(xy);
        drill = -1;
    }

    public void drill(Hero hero) {
        this.drilledBy = hero;
        drill = 0;
    }

    @Override
    public void tick() {
        if (drill == -1) {
            drilledBy = null;
        }

        if (drill != -1) {
            drill++;
            if (drill == DRILL_TIMER) {
                drill = -1;
            }
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (drill == 1) {
            return Elements.DRILL_PIT;
        } else if (drill > 1) {
            switch (Brick.DRILL_TIMER - drill) {
                case 1 : return Elements.PIT_FILL_1;
                case 2 : return Elements.PIT_FILL_2;
                case 3 : return Elements.PIT_FILL_3;
                case 4 : return Elements.PIT_FILL_4;
                default: return Elements.NONE;
            }
        } else {
            return Elements.BRICK;
        }
    }

    public Hero getDrilledBy() {
        return drilledBy;
    }
}
