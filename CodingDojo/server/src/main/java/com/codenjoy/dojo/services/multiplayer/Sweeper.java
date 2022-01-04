package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiPredicate;

import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.RELOAD_ALONE;

@Getter
@RequiredArgsConstructor
public class Sweeper {

    private MultiplayerType type;
    private final boolean resetOther;
    private BiPredicate<Deal, List<Deal>> applicants;

    /**
     * Любая работа с юзером в комнате (удаление или перезапуск) происходит
     * с перегрузкой оставшихся на том же поле других участников.
     * Критерий отбора кандидатов задается отдельно.
     */
    public static Sweeper on() {
        return new Sweeper(RELOAD_ALONE);
    }

    /**
     * Любая работа с юзером в комнате (удаление или перезапуск) происходит
     * с игнорированием оставшихся на том же поле других участников.
     */
    public static Sweeper off() {
        return new Sweeper(!RELOAD_ALONE).noOne();
    }

    /**
     * Никто из оставшихся на поле не будет обрабатываться.
     */
    public Sweeper noOne() {
        applicants = (player, players) -> false;
        return this;
    }

    /**
     * Работа проводится только с последним оставшимся на поле,
     * если на то будет воля MultiplayerType.
     */
    public Sweeper lastAlone() {
        applicants = (player, players) -> type.shouldReloadAlone() && players.size() == 1;
        return this;
    }

    /**
     * Работа проводится со всеми оставшимся на пооле независимо от
     * MultiplayerType.
     */
    public Sweeper allRemaining() {
        applicants = (player, players) -> true;
        return this;
    }

    /**
     * Задаем MultiplayerType для последующего анализа.
     */
    public Sweeper of(MultiplayerType type) {
        this.type = type;
        return this;
    }
}
