package com.codenjoy.dojo.clifford.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.clifford.services.levels.Big;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapLoader {

    protected static final Logger log = LoggerFactory.getLogger(MapLoader.class);

    public static String loadMapFromFile(String path) {
        try {
            StringBuilder map = new StringBuilder();
            Scanner scanner = new Scanner(new File(path), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                map.append(line);
            }
            return map.toString();
        } catch (FileNotFoundException e) {
            log.warn("Map loading error", e);
        }
        return Big.all().get(0);
    }

}
