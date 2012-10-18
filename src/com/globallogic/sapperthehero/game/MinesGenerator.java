package com.globallogic.sapperthehero.game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MinesGenerator {
    public List<Mine> get(int count, Board board);

}
