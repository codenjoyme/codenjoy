package com.codenjoy.dojo.snake.battle.model.objects;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.battle.model.Elements;
import com.codenjoy.dojo.snake.battle.model.Player;

/**
 * @author Kors
 */
public class StartFloor extends PointImpl implements State<Elements, Player> {

    public StartFloor(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.START_FLOOR;
    }
}
