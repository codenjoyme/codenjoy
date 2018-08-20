package com.codenjoy.dojo.sokoban.model.itemsImpl;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.sokoban.services.Player;

public class BoxOnTheMark extends PointImpl implements State<Elements, Player> {
    private boolean isActive;

    public BoxOnTheMark(int x, int y) {
        super(x, y);
    }

    public BoxOnTheMark(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BOX_ON_THE_MARK;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
