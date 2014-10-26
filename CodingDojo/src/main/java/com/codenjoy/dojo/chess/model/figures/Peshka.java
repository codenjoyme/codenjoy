package com.codenjoy.dojo.chess.model.figures;

import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.chess.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

public class Peshka extends Figure implements State<Elements, Player> {

    public Peshka(Point xy, boolean isWhite) {
        super(xy, isWhite);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (isWhite()) {
            return Elements.WHITE_PESHKA;
        } else {
            return Elements.BLACK_PESHKA;
        }
    }
}