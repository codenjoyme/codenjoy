package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Created by oleksandr.baglai on 23.06.2016.
 */
public class SecondGameType implements GameType {
    @Override
    public PlayerScores getPlayerScores(int score) {
        return null;
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        return null;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return null;
    }

    @Override
    public String name() {
        return "second";
    }

    public enum Elements implements CharElements {

        NONE(' '),
        RED('R'),
        GREEN('G'),
        BLUE('B');

        final char ch;

        Elements(char ch) {
            this.ch = ch;
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

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return null;
    }

    @Override
    public boolean isSingleBoard() {
        return false;
    }

    @Override
    public boolean newAI(String aiName) {
        return false;
    }
}
