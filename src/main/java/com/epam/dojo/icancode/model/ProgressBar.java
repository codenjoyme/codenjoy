package com.epam.dojo.icancode.model;

import com.epam.dojo.icancode.services.Levels;
import com.epam.dojo.icancode.services.Printer;

/**
 * Created by oleksandr.baglai on 27.06.2016.
 */
public class ProgressBar {

    private Player player;

    private int currentLevel;
    private int lastPassedLevel;
    private boolean finished;
    private boolean nextLevel;

    private Integer backToSingleLevel;

    private ICanCode single;
    private ICanCode multiple;
    private ICanCode current;
    private Printer printer;

    public ProgressBar(ICanCode single, ICanCode multiple) {
        this.single = single;
        this.multiple = multiple;

        current = single;
        clearFinished();
        backToSingleLevel = null;
        currentLevel = 0;
        lastPassedLevel = -1;
        loadLevel();
        buildPrinter();
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public void clearNextLevel() {
        nextLevel = false;
    }

    public void setNextLevel() {
        nextLevel = true;
    }

    private void buildPrinter() {
        printer = new Printer(current, Levels.size());
    }

    public Integer getBackToSingleLevel() {
        Integer result = backToSingleLevel;
        backToSingleLevel = null;
        return result;
    }

    public void clearFinished() {
        finished = false;
    }

    private void loadLevel() {
        ILevel level = current.getLevels().get(currentLevel);
        current.setLevel(level);
        if (current.getPlayers().isEmpty()) {
            level.init(current);
        }
    }

    void checkLevel() {
        if (isNextLevel()) {
            if (currentLevel < current.getLevels().size() - 1) {
                if (lastPassedLevel < currentLevel) {
                    lastPassedLevel = currentLevel;
                }
                currentLevel++;
                loadLevel();
            } else {
                if (!current.isMultiple()) {
                    if (lastPassedLevel < currentLevel) {
                        lastPassedLevel = currentLevel;
                    }
                    finished = true;
                }
            }
            player.newHero(current);
            clearNextLevel();
        } else if (!player.getHero().isAlive()) {
            player.newHero(current);
            clearNextLevel();
        } else if (player.getHero().isChangeLevel()) {
            int level = player.getHero().getLevel();
            if (level == -1) {
                level = currentLevel;
            }
            if (!current.isMultiple()) {
                if (level > lastPassedLevel + 1) {
                    return;
                }
                if (level >= current.getLevels().size()) {
                    finished = true;
                    return;
                }
                currentLevel = level;
                loadLevel();
                player.newHero(current);
                clearNextLevel();
            } else {
                backToSingleLevel = level;
            }
        }
    }

    public void tick() {
        current.tick();
        if (current == multiple) {
            Integer level = getBackToSingleLevel();
            if (level != null) {
                if (level > single.getLevels().size()) {
                    return;
                }
                remove(player);
                current = single;
                clearFinished();
                buildPrinter();
                newGame(player);
                player.getHero().loadLevel(level);
                checkLevel();
            }
        } else {
            if (finished) {
                remove(player);
                current = multiple;
                currentLevel = 0;
                loadLevel();
                buildPrinter();
                newGame(player);
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void newGame(Player player) {
        current.newGame(player);
    }

    public void remove(Player player) {
        current.remove(player);
    }

    public Printer getPrinter() {
        return printer;
    }
}
