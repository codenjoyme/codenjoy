package com.codenjoy.dojo.excitebike.services.parse;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.excitebike.model.items.Accelerator;
import com.codenjoy.dojo.excitebike.model.items.Border;
import com.codenjoy.dojo.excitebike.model.items.Inhibitor;
import com.codenjoy.dojo.excitebike.model.items.LineChanger;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElement;

import java.util.List;

public interface MapParser {

    int getXSize();

    int getYSize();

    List<Bike> getFallenBikes();

    List<Accelerator> getAccelerators();

    List<Border> getBorders();

    List<Inhibitor> getInhibitors();

    List<LineChanger> getLineUpChangers();

    List<LineChanger> getLineDownChangers();

    List<Obstacle> getObstacles();

    List<SpringboardElement> getSpringboardDarkElements();

//    List<SpringboardElement> getSpringboardDarkFrontElements();

    List<SpringboardElement> getSpringboardLightElements();

    List<SpringboardElement> getSpringboardLeftDownElements();

    List<SpringboardElement> getSpringboardLeftUpElements();

    List<SpringboardElement> getSpringboardRightDownElements();

    List<SpringboardElement> getSpringboardRightUpElements();

    List<SpringboardElement> getSpringboardNoneElements();

}
