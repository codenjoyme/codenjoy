package com.codenjoy.dojo.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

@UtilityClass
public class Encoding {
    
    public static final String UTF8 = "UTF-8";

    public static String replaceN(String value) {
        return value.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\r\n", "\n")
                .replace("\n\r", "\n")
                .replace("\r", "\n");
    }

    public static String removeN(String value) {
        return value.replaceAll("\n|\r", "");
    }


}
