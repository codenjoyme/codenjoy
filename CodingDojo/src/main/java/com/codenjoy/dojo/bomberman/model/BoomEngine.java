package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 11:58 PM
 */
public interface BoomEngine {

    List<Blast> boom(List<? extends PointImpl> barriers, int boardSize, PointImpl source, int radius);

}
