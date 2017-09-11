package com.epam.dojo.expansion.model.lobby;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.LevelImpl;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.services.SettingsWrapper;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class LobbyPlayerBoard implements PlayerBoard {
    private List<Player> waiting;
    private LevelImpl level;

    public LobbyPlayerBoard(List<Player> waiting) {
        this.waiting = waiting;
        level = Levels.getLevel(size(), "MULTILOBBY");
    }

    @Override
    public int getViewSize() {
        return 20; // TODO надо это где-то взять
    }

    @Override
    public int size() {
        return 20; // TODO надо это где-то взять
    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    public int levelsCount() {
        return 0;
    }

    @Override
    public void newGame(Player player) {
        Hero hero = new Hero() {
            @Override
            public Point getPosition() {
                return pt(0, 0);
            }

            @Override
            public void tick() {
                // do nothing
            }
        };
        player.setHero(hero);
        hero.setField(new Lobby());
    }

    @Override
    public void remove(Player player) {
        // do nothing
    }

    @Override
    public void loadLevel(int level) {
        // do nothing
    }

    @Override
    public List<Player> getPlayers() {
        return waiting;
    }

    @Override
    public int getRoundTicks() {
        return SettingsWrapper.UNLIMITED;
    }

    @Override
    public String id() {
        return "ELB@" + Integer.toHexString(this.hashCode());
    }

    @Override
    public void tick() {
        // do nothing
    }
}
