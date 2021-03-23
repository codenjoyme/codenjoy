package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO перенести в utils пакет
public class StateUtils {

	public static <T> List<T> filter(Object[] array, Class<T> clazz) {
		return (List) Arrays.stream(array)
				.filter(it -> it != null)
				.filter(it -> clazz.isAssignableFrom(it.getClass()))
				.collect(toList());
	}

	public static <T> T filterOne(Object[] array, Class<T> clazz) {
		return (T) Arrays.stream(array)
				.filter(it -> it != null)
				.filter(it -> clazz.isAssignableFrom(it.getClass()))
				.findFirst()
				.orElse(null);
	}

	public static boolean itsMe(GamePlayer player, PlayerHero hero,
								Object[] alsoAtPoint, State item)
	{
		return player.getHero() == hero
				|| Arrays.asList(alsoAtPoint).contains(item);
	}
}
