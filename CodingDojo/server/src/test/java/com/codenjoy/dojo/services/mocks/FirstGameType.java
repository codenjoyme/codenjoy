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

import static org.mockito.Mockito.when;

public class FirstGameType extends AbstractGameType<SettingsImpl> {

    private final SimpleParameter<Integer> size = new SimpleParameter<>(23);

    @Override
    public SettingsImpl getSettings() {
        return new FirstGameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, SettingsImpl settings) {
        return new FakePlayerScores(score);
    }

    class Field implements GameField<Player> {

        private Player player;
        private SettingsReader settings;

        public Field(SettingsImpl settings) {
            this.settings = (SettingsReader) settings;
        }

        @Override
        public BoardReader reader() {
            return new BoardReader() {
                @Override
                public int size() {
                    return size.getValue();
                }

                @Override
                public Iterable<? extends Point> elements() {
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
            return settings;
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
    public GameField createGame(int levelNumber, SettingsImpl settings) {
        return Mockito.spy(new Field(settings));
    }

    @Override
    public Parameter<Integer> getBoardSize(SettingsImpl settings) {
        return size;
    }

    @Override
    public String name() {
        return "first";
    }

    public enum Elements implements CharElements {

        NONE(' '),
        WALL('☼'),
        HERO('☺');

        final char ch;

        Elements(char ch) {
            this.ch = ch;
        }

        public static Elements valueOf(char ch) {
            return Arrays.stream(Elements.values())
                    .filter(el -> el.ch == ch)
                    .findFirst()
                    .get();
        }

        @Override
        public char ch() {
            return ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

    }

    class ClientBoard extends AbstractBoard<Elements> {
        @Override
        public Elements valueOf(char ch) {
            return Elements.valueOf(ch);
        }
    }

    @Override
    public Elements[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return ClientBoard.class;
    }

    @Override
    public MultiplayerType getMultiplayerType(SettingsImpl settings) {
        return MultiplayerType.SINGLE;
    }

    class Hero extends PlayerHero implements NoDirectionJoystick, State<Elements, Player> {

        @Override
        public void act(int... p) {

        }

        @Override
        public void tick() {

        }

        @Override
        public Elements state(Player player, Object... alsoAtPoint) {
            return Elements.HERO;
        }
    }

    class Player extends GamePlayer {

        Hero hero;

        public Player(EventListener listener, SettingsReader settings) {
            super(listener, settings);
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
    public GamePlayer createPlayer(EventListener listener, String playerId, SettingsImpl settings) {
        return Mockito.spy(new Player(listener, (SettingsReader) settings));
    }

    @Override
    public String getVersion() {
        return "version 1.11b";
    }
}
