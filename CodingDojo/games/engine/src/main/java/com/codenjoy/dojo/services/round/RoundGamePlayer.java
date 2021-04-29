package com.codenjoy.dojo.services.round;

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

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsReader;

public abstract class RoundGamePlayer<H extends RoundPlayerHero, F extends GameField> extends GamePlayer<H, F> {

    private boolean shouldLeave;
    private Parameter<Boolean> roundsEnabled;

    public RoundGamePlayer(EventListener listener, SettingsReader settings) {
        super(listener, settings);
        this.roundsEnabled = settings.boolValue(RoundSettings.Keys.ROUNDS_ENABLED);
        shouldLeave = false;
    }

    public void start(int round, Object startEvent) {
        event(startEvent);
        printMessage("Round " + round);
        hero.setActive(true);
    }

    @Override
    public boolean shouldLeave() {
        return shouldLeave;
    }

    public void leaveBoard() {
        shouldLeave = true;
    }

    public boolean isActive() {
        return hero != null && hero.isActive();
    }

    @Override
    public boolean wantToStay() {
        return roundsEnabled();
    }

    public Boolean roundsEnabled() {
        return roundsEnabled.getValue();
    }

    public void die(boolean lastRound, Object dieEvent) {
        event(dieEvent);
        shouldLeave = lastRound;
    }

    @Override
    public void setHero(H hero) {
        super.setHero(hero);
        if (hero != null) {
            hero.setPlayer(this);
        }
    }

    @Override
    public void newHero(F field) {
        if (shouldCreate()) {
            if (hero != null) {
                hero.setPlayer(null);
            }
        }

        super.newHero(field);

        hero.setPlayer(this);

        if (!roundsEnabled()) {
            hero.setActive(true);
        }
    }

}
