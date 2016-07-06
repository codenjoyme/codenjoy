package com.epam.dojo.icancode.model;

import com.epam.dojo.icancode.model.interfaces.ILevel;
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
        finished = false;
        backToSingleLevel = null;
        currentLevel = 0;
        lastPassedLevel = -1;
        loadLevel();
        buildPrinter();
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

    private void loadLevel() {
        ILevel level = current.getLevels().get(currentLevel);
        current.setLevel(level);

        if (!current.isMultiple() || current.isMultiple() && current.getPlayers().isEmpty()) {
            level.setField(current);
        }
    }

    void checkLevel() {
        if (nextLevel) {
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
            nextLevel = false;
        } else if (!player.getHero().isAlive()) {
            player.newHero(current);
            nextLevel = false;
        } else if (player.getHero().isChangeLevel()) {
            int level = player.getHero().getLevel();
            if (!current.isMultiple()) {
                if (level == -1) {
                    level = currentLevel;
                }
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
                nextLevel = false;
            } else {
                if (level != -1) {
                    backToSingleLevel = level;
                } else {
                    loadLevel();
                    player.newHero(current);
                    nextLevel = false;
                }
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
                finished = false;
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

    public String printProgress() { // TODO test me
        return "{\"current\":" + currentLevel + ", " +
                "\"total\":" + (single.getLevels().size() + 1) + "}";
    }
}
