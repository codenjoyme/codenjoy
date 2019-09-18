package com.codenjoy.dojo.loderunner.services;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MapLoader {

  public static String loadMapFromFile(String mapFilePath) {
    try {
      StringBuilder map = new StringBuilder();
      System.out.println("File Path - " + mapFilePath);
      Scanner scanner = new Scanner(new File(new URI("file:///C:/Work/epamMap.txt")));
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println("File Line - " + line);
        map.append(line);
      }
      System.out.println("4 New map - " + map.toString());
    } catch (FileNotFoundException | URISyntaxException e) {
      //TODO: log
      e.printStackTrace();
    }
    System.out.println("5");
    return Level2.get();
  }

}
