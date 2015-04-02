package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

/**
 * User: sanja
 * Date: 07.01.14
 * Time: 16:17
 */
public interface EnemyAI {

    Direction getDirection(Field field, Hero hero, Point me);

}
