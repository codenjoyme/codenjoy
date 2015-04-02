package com.codenjoy.dojo.chess.model.figures;

import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.chess.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

public class Ferz extends Figure implements State<Elements, Player> {

    public Ferz(Point xy, boolean isWhite) {
        super(xy, isWhite);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (isWhite()) {
            return Elements.WHITE_FERZ;
        } else {
            return Elements.BLACK_FERZ;
        }
    }
}
