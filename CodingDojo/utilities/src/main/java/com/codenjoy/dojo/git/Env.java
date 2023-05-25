package com.codenjoy.dojo.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Env {

    public static Properties load(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file).getAbsolutePath()))) {
            String line;
            Map<String, String> envMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    int equalsIndex = line.indexOf('=');
                    if (equalsIndex != -1) {
                        String key = line.substring(0, equalsIndex).trim();
                        String value = line.substring(equalsIndex + 1).trim();
                        envMap.put(key, value);
                    }
                }
            }

            Properties properties = new Properties();
            properties.putAll(envMap);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
