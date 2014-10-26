package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Mine;

import java.util.List;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:52 PM
 */
public interface MinesGenerator {
    List<Mine> get(int count, Field board);
}
