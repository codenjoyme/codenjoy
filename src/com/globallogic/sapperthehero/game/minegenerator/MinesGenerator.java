package com.globallogic.sapperthehero.game.minegenerator;

import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Mine;

import java.util.List;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:52 PM
 */
public interface MinesGenerator {
    public List<Mine> get(int count, Board board);

}
