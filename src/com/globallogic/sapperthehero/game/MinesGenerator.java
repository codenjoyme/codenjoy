package com.globallogic.sapperthehero.game;

import java.util.List;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:52 PM
 */
public interface MinesGenerator {
    public List<Mine> get(int count, Board board);

}
