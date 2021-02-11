package com.codenjoy.dojo.expansion.model.levels.items;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.levels.Item;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Oleksandr_Baglai on 2017-12-12.
 */
public class ForcesState implements State<String, Player> {

    public static final int COUNT_NUMBERS = TestUtils.COUNT_NUMBERS;

    private Item item;

    public ForcesState(Item item) {
        this.item = item;
    }

    @Override
    public String state(Player player, Object... alsoAtPoint) {
        if (item != null && item instanceof HeroForces) {
            HeroForces forces = (HeroForces) item;
            int count = forces.getForces().getCount();
            String result = Integer.toString(count, Character.MAX_RADIX).toUpperCase();
            if (result.length() < COUNT_NUMBERS) { // TODO оптимизировать
                return StringUtils.leftPad(result, COUNT_NUMBERS, '0');
            } else if (result.length() > COUNT_NUMBERS) {
                return result.substring(result.length() - COUNT_NUMBERS, result.length());
            }
            return result;
        } else {
            return "-=#";
        }
    }

    public static int parseCount(String sub) {
        if (sub.equals("-=#")) {
            return 0;
        } else {
            return Integer.parseInt(sub, Character.MAX_RADIX);
        }
    }


}
