package com.codenjoy.dojo.services;

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

import lombok.experimental.UtilityClass;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@UtilityClass
public class VersionReader {

    public static final String NO_VERSION = "no version";
    public static final String KEY_VERSION = "version";
    public static final String KEY_TIME = "time";
    public static final String KEY_REVISION = "revision";
    public static final String KEY_BRANCH = "branch";

    public static JSONObject version(List<String> components) {
        return new JSONObject(){{
            components.forEach(name -> put(name, VersionReader.version(name)));
        }};
    }

    public static String versionReadable(String component) {
        JSONObject json = version(component);
        return String.format("%s_%s_%s",
                json.getString(KEY_VERSION),
                json.getString(KEY_TIME),
                json.getString(KEY_REVISION));
    }

    public static String getWelcomeMessage() {
        String version = versionReadable("engine");
        return  "\n" +
                "     /\\  ______          _              _               _   \n" +
                "    //\\\\/ _____)        | |            (_)            _| |_ \n" +
                "   (____)/       ___  _ | | ____ ____   _  ___  _   _(  ___)\n" +
                "       | |      / _ \\/ || |/ _  )  _ \\ | |/ _ \\| | | |___  )\n" +
                "       | \\_____( (_) )(_| | (/ /| | | || | (_) ) |_| |_   _)\n" +
                "        \\______)\\___/\\____|\\____)_| |_|| |\\___/ \\__  | |_|  \n" +
                "                                      _| |      __/ /\n" +
                " ====================================(__/======(___/===========\n" +
                "  :: Codenjoy :: (Version " + version + ")\n" +
                "    :: Fork me on https://github.com/codenjoyme/codenjoy :: \n" +
                "   --------------------------------------------------------- \n";
    }

    public static JSONObject version(String component) {
        try {
            Properties properties = new Properties();

            InputStream stream = VersionReader.class.getClassLoader().getResourceAsStream(component + "/version.properties");
            if (stream == null) {
                return noVersion();
            }
            properties.load(stream);

            return new JSONObject(){
                {
                    putIfExists(KEY_VERSION);
                    putIfExists(KEY_TIME);
                    putIfExists(KEY_REVISION);
                    putIfExists(KEY_BRANCH);
                }

                private void putIfExists(String name) {
                    String data = properties.getProperty(name);
                    if (data == null) {
                        return;
                    }
                    if (KEY_REVISION.equals(name)) {
                        data = data.substring(0, 8);
                    }
                    put(name, data);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return noVersion();
    }

    private static JSONObject noVersion() {
        return new JSONObject(){{
            put("version", NO_VERSION);
        }};
    }
}
