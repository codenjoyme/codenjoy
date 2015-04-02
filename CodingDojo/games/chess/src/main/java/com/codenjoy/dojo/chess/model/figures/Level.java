package com.codenjoy.dojo.chess.model.figures;

import java.util.List;

public interface Level {

    int getSize();

    List<Figure> getFigures(boolean isWhite);
}
