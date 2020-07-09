package com.codenjoy.dojo;

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

import org.json.JSONArray;
import org.json.JSONObject;

import static com.codenjoy.dojo.FinalistsSorter.load;

public class GetEmails {

    public static void main(String[] args) {
        String day = "day5_2020-06-19";
        JSONArray users = load(day + "/users.txt");

        for (int i = 0; i < users.length(); i++) {
            JSONObject json = users.getJSONObject(i);
            System.out.println(json.get("email"));
        }
    }
}
