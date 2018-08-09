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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by indigo on 04.08.2016.
 */
public class VersionReader {

    public static final String NO_VERSION = "no version";

    public static String getCurrentVersion(String gameName) {
        try {
            Properties properties = new Properties();
            InputStream stream = VersionReader.class.getClassLoader().getResourceAsStream(gameName + "/version.properties");
            if (stream == null) {
                return NO_VERSION;
            }
            properties.load(stream);
            return (String) properties.get("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NO_VERSION;
    }
}
