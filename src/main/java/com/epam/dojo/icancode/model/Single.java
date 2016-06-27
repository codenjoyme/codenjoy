package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PrinterFactory;
import com.epam.dojo.icancode.services.Levels;
import com.epam.dojo.icancode.services.Printer;

/**
 * А вот тут немного хак :) Дело в том, что фреймворк изначально не поддерживал игры типа "все на однмо поле", а потому
 * пришлось сделать этот декоратор. Борда (@see Sample) - одна на всех, а игры (@see Single) у каждого своя. Кода тут не много.
 */
public class Single implements Game {

    private Player player;
    private ICanCode single;
    private ICanCode multiple;
    private ICanCode current;
    private Printer printer;

    public Single(ICanCode single, ICanCode multiple, EventListener listener, PrinterFactory factory) {
        this.single = single;
        this.multiple = multiple;
        this.current = single;

        this.player = new Player(listener);
        buildPrinter();
    }

    private void buildPrinter() {
        printer = new Printer(current, Levels.size());
    }

    @Override
    public Joystick getJoystick() {
        return player.getHero();
    }

    @Override
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
    }

    @Override
    public boolean isGameOver() {
        return !player.hero.isAlive();
    }

    @Override
    public void newGame() {
        current.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.print(player);
    }

    @Override
    public void destroy() {
        current.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public Point getHero() {
        return player.getHero().getPosition();
    }

    @Override
    public void tick() {
        if (current == multiple) {
            current.tick();
            Integer level = current.getBackToSingleLevel();
            if (level != null) {
                if (level > current.getLevels().size()) {
                    return;
                }
                destroy();
                current = single;
                current.clearFinished();
                buildPrinter();
                newGame();
                player.getHero().loadLevel(level + 1);
                current.checkLevel(player);
            }
        } else {
            current.tick();
            if (current.finished()) {
                destroy();
                current = multiple;
                buildPrinter();
                newGame();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Printer getPrinter() {
        return printer;
    }
}
