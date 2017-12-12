package com.epam.dojo.expansion.model.levels.items;

import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.levels.Cell;
import com.epam.dojo.expansion.model.levels.Item;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import org.apache.commons.lang.StringUtils;

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
