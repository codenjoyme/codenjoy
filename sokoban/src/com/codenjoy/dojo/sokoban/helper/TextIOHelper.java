package com.codenjoy.dojo.sokoban.helper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static java.util.logging.Level.*;


public class TextIOHelper {
    protected static Logger log = Logger.getLogger(TextIOHelper.class.getName());

   public static String getStringFromResourcesTxt(int level) {
        String result = "EMPTY";
        ClassLoader classLoader = TextIOHelper.class.getClassLoader();
       try {
           URI uri = classLoader.getResource("sokoban/level" + level + ".txt").toURI();
//           alternate way
//           String pathHandledStr = System.getProperty( "os.name" ).contains( "indow" ) ? uri.toString().substring(6) : uri.toString();
//           Path pathHandled = Paths.get(pathHandledStr);
           Path path = Paths.get(uri);
          result = String.join("", Files.readAllLines(path));

       } catch (IOException|URISyntaxException e) {
           log.log(ALL, e.getMessage());
       }
        return result;
    }

    public static String getStringFromResourcesRtf(int level) {
        String result = "EMPTY";
        ClassLoader classLoader = TextIOHelper.class.getClassLoader();
        try {
            URI uri = classLoader.getResource("sokoban/level" + level + ".rtf").toURI();
//           alternate way
//           String pathHandledStr = System.getProperty( "os.name" ).contains( "indow" ) ? uri.toString().substring(6) : uri.toString();
//           Path pathHandled = Paths.get(pathHandledStr);
            Path path = Paths.get(uri);
            result = String.join("", Files.readAllLines(path));

        } catch (IOException|URISyntaxException e) {
            log.log(ALL, e.getMessage());
        }
        return result;
    }

    public static String getStringFromResourcesRtf(String nameOfTextResource) {
        String result = "EMPTY";
        ClassLoader classLoader = TextIOHelper.class.getClassLoader();
        Path path;

        try {
            if (!nameOfTextResource.isEmpty()) {
                path = Paths.get(classLoader.getResource(nameOfTextResource).toURI());
            }
            else {
                path = Paths.get(classLoader.getResource("sokoban/level0.rtf").toURI());
            }
            result = String.join("", Files.readAllLines(path));

        } catch (IOException|URISyntaxException e) {
            log.log(ALL, e.toString());
        }
        return result;
     }

}