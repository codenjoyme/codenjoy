package com.codenjoy.dojo.web.rest.pojo;

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

import com.codenjoy.dojo.services.Deal;
import lombok.AllArgsConstructor;
import org.json.JSONObject;

@AllArgsConstructor
public class PScoresOf {

    private Deal deal;

    public String getId() {
        return deal.getPlayer().getId();
    }

    public String getName() {
        return deal.getPlayer().getReadableName();
    }

    public int getTeamId() {
        return deal.getTeamId();
    }

    public int getScore() {
        Object score = deal.getPlayer().getScore();
        if (score instanceof JSONObject) {
            // это для игрушек, которые передают чуть больше инфы на фронт об очках
            return ((JSONObject)score).getInt("score");
        } else {
            try {
               return Integer.valueOf(score.toString());
            } catch (Exception e) {
                return -1;
            }

        }
    }

}
