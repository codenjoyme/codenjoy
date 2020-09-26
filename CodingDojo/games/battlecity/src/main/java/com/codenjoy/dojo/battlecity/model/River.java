package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class River extends PointImpl implements State<Elements, Player> {

	public River(int x, int y) {
		super(x, y);
	}

	@Override
	public Elements state(Player player, Object... alsoAtPoint) {
		return Elements.RIVER;
	}
}
