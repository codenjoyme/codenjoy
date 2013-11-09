package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class BattlecityGame implements GameType {

    public final static int BATTLE_FIELD_SIZE = 15;

    private Tanks tanks;

    public BattlecityGame() {
        tanks = new Tanks(BATTLE_FIELD_SIZE, getConstructions(),
                new AITank(5, 5, Direction.DOWN),
                new AITank(11, 10, Direction.DOWN));
    }

    private List<Construction> getConstructions() {
        List<Construction> constructions = new LinkedList<Construction>();
        line(constructions, 2);
        line(constructions, 4);
        line(constructions, 6);
        line(constructions, 8);
        line(constructions, 10);
        line(constructions, 12);
        return constructions;
    }

    private void line(List<Construction> constructions, int x) {
        constructions.add(new Construction(x, 2));
        constructions.add(new Construction(x, 3));
        constructions.add(new Construction(x, 4));
        constructions.add(new Construction(x, 5));
        constructions.add(new Construction(x, 6));
        constructions.add(new Construction(x, 7));
        constructions.add(new Construction(x, 8));
        constructions.add(new Construction(x, 9));
        constructions.add(new Construction(x, 10));
        constructions.add(new Construction(x, 11));
        constructions.add(new Construction(x, 12));
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BattlecityPlayerScores(score);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleTanks(tanks, listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(BATTLE_FIELD_SIZE);
    }

    @Override
    public String gameName() {
        return "battlecity";
    }

    @Override
    public Object[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        throw new UnsupportedOperationException();  // TODO implement me
    }

    @Override
    public boolean isSingleBoardGame() {
        return true;
    }
}
