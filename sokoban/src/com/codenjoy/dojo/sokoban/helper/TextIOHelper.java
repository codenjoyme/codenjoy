package com.codenjoy.dojo.sokoban.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static java.util.logging.Level.*;


public class TextIOHelper {
    protected static Logger log = Logger.getLogger(TextIOHelper.class.getName());
//    LogManager.getLogManager().readConfiguration(TextIOHelper.class.getResourceAsStream("logger.properties"));

   public static String getStringFromResources(int level) {
        String result = "EMPTY";
        ClassLoader classLoader = TextIOHelper.class.getClassLoader();
        String nameOfTextResource;
        Path path = Paths.get(classLoader.getResource("sokoban/level"+level+".txt").getPath());

        try {
            result = Files.readAllLines(path).toString().replace("\n","");

        } catch (IOException e) {
            log.log(WARNING, e.toString());
        }
        return result;
    }

  public static String getStringFromResources(String nameOfTextResource) {
        String result = "EMPTY";
        ClassLoader classLoader = TextIOHelper.class.getClassLoader();
        Path path;
        if (!nameOfTextResource.isEmpty()) {
            path = Paths.get(classLoader.getResource(nameOfTextResource).getPath());
        }
        else {
            path = Paths.get(classLoader.getResource("sokoban/level0.txt").getPath());
        }
        try {
            result = Files.readAllLines(path).toString().replace("\n","");

        } catch (IOException e) {
            log.log(WARNING, e.toString());
        }
        return result;
     }

}