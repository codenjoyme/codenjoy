package com.codenjoy.dojo.services.mocks;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.NoDirectionJoystick;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.*;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class FakeGameType extends AbstractGameType<Settings> {

    @Override
    public PlayerScores getPlayerScores(Object score, Settings settings) {
        return new FakePlayerScores(score);
    }

    class Field implements GameField<Player> {

        private Player player;
        private Settings settings;

        public Field(Settings settings) {
            this.settings = settings;
        }

        @Override
        public BoardReader reader() {
            return new BoardReader<Player>() {
                @Override
                public int size() {
                    return getBoardSize(settings).getValue();
                }

                @Override
                public Iterable<? extends Point> elements(Player player) {
                    return new LinkedList<>() {{
                        add(Field.this.hero());
                    }};
                }
            };
        }

        private Hero hero() {
            return (Hero) player.getHero();
        }

        @Override
        public void newGame(Player player) {
            this.player = player;
            player.newHero(this);
        }

        @Override
        public void remove(Player player) {
            this.player = null;
        }

        @Override
        public SettingsReader settings() {
            return (SettingsReader) settings;
        }

        @Override
        public void tick() {
            if (player == null || player.getHero() == null) {
                return;
            }

            player.getHero().tick();
        }
    }

    @Override
    public GameField createGame(int levelNumber, Settings settings) {
        return Mockito.spy(new Field(settings));
    }

    @Override
    public abstract Parameter<Integer> getBoardSize(Settings settings);

    @Override
    public abstract String name();

    public class ClientBoard extends AbstractBoard<CharElements> {

        @Override
        public CharElements valueOf(char ch) {
            return FakeGameType.this.valueOf(ch);
        }
    }

    public CharElements valueOf(char ch) {
        return Arrays.stream(getPlots())
                .filter(el -> el.ch() == ch)
                .findFirst()
                .get();
    }

    @Override
    public abstract CharElements[] getPlots();

    @Override
    public Class<? extends Solver> getAI() {
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return ClientBoard.class;
    }

    @Override
    public abstract MultiplayerType getMultiplayerType(Settings settings);

    class Hero extends PlayerHero implements NoDirectionJoystick, State<CharElements, Player> {

        public Hero() {
            super(heroAt());
        }

        @Override
        public void act(int... p) {

        }

        @Override
        public void tick() {

        }

        @Override
        public CharElements state(Player player, Object... alsoAtPoint) {
            return getHeroElement();
        }
    }

    public abstract Point heroAt();

    public abstract CharElements getHeroElement();

    class Player extends GamePlayer {

        Hero hero;

        public Player(EventListener listener, Settings settings) {
            super(listener, (SettingsReader) settings);
        }

        @Override
        public PlayerHero getHero() {
            return hero;
        }

        @Override
        public void newHero(GameField field) {
            hero = new Hero();
            hero.init(field);
        }

        @Override
        public boolean isAlive() {
            return true;
        }
    };

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId, Settings settings) {
        return Mockito.spy(new Player(listener, settings));
    }

    @Override
    public abstract String getVersion();
}
