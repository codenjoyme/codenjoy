package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BattlecityTest {

    protected Dice dice = mock(Dice.class);
    public int ticksPerBullets;
    public int size;
    private Parameter<Integer> spawnAiPrize;
    private Parameter<Integer> hitKillsAiPrize;

    private Battlecity game;
    private Joystick hero;
    private List<Player> players = new LinkedList<>();
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Before
    public void setup() {
        size = 7;
        ticksPerBullets = 1;
        spawnAiPrize = v(4);
        hitKillsAiPrize = v(3);
    }

    private List<Border> borders() {
        return new DefaultBorders(size).get();
    }

    private void givenGame(Tank tank, Wall... walls) {
        game = new Battlecity(size, dice, Arrays.asList(walls), borders(), spawnAiPrize, hitKillsAiPrize);
        initPlayer(game, tank);
        this.hero = tank;
    }

    private void givenGameWithAI(Tank tank, Point... ais) {
        game = new Battlecity(size, dice, Arrays.asList(new Wall[0]), borders(), spawnAiPrize, hitKillsAiPrize, ais);
        initPlayer(game, tank);
        this.hero = tank;
    }

    private void givenGameWith(Tank... tanks) {
        game = new Battlecity(size, dice, Arrays.asList(new Wall[0]),
                borders(), spawnAiPrize, hitKillsAiPrize);

        for (Tank tank : tanks) {
            initPlayer(game, tank);
        }
        this.hero = tanks[0];
    }

    private Player initPlayer(Battlecity game, Tank tank) {
        Player player = mock(Player.class);
        when(player.getHero()).thenReturn(tank);
        players.add(player);
        tank.init(game);
        game.newGame(player);
        return player;
    }

    public static Tank tank(int x, int y, Direction direction, int ticksPerBullets) {
        Dice dice = getDice(x, y);
        return new Tank(pt(x, y), direction, dice, ticksPerBullets);
    }

    public Tank tank(int x, int y, Direction direction) {
        return tank(x, y, direction, ticksPerBullets);
    }

    private static Dice getDice(int x, int y) {
        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(x, y);
        return dice;
    }

    private static Dice getDice(Point pt, int indexPrizes) {
        Dice dice = mock(Dice.class);
        when(dice.next(anyInt()))
                .thenAnswer(x -> pt.getX())
                .thenAnswer(y -> pt.getY())
                .thenAnswer(ans -> indexPrizes);
        return dice;
    }

    private String getPrizesCount() {
        List<Tank> tanks = game.getTanks();
        long prizes = tanks.stream().filter(x -> x.isTankPrize()).count();

        return String.format("%s prizes with %s tanks", prizes, tanks.size());
    }

    private void givenGameBeforeDropPrize(Point pt) {
        Bullet bullet = mock(Bullet.class);
        Dice dice = getDice(pt, 0);
        hitKillsAiPrize = v(1);
        Tank tank = tank(1, 1, Direction.UP);
        Point ai = pt(1, 5);

        game = new Battlecity(size, dice, Arrays.asList(new Wall(3, 3)), borders(), spawnAiPrize, hitKillsAiPrize);
        initPlayer(game, tank);
        this.hero = tank;
        Tank aiTank = game.getAiGenerator().drop(ai);
        aiTank.kill(bullet);
    }

    @Test
    public void shouldDrawField() {
        givenGame(tank(1, 1, Direction.UP));
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void assertD(String field) {
        assertEquals(field, getPrinter().print());
    }

    private Printer<String> getPrinter() {
        return printerFactory.getPrinter(
                game.reader(), players.get(0));
    }

    @Test
    public void shouldBeWall_whenGameCreated() {
        givenGame(tank(1, 1, Direction.UP), new Wall(3, 3));
        assertEquals(1, game.getWalls().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        givenGame(tank(1, 1, Direction.UP));
        assertNotNull(game.getTanks());
    }

    @Test
    public void shouldTankMove() {
        givenGame(tank(1, 1, Direction.UP));
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankStayAtPreviousPositionWhenIsNearBorder() {
        givenGame(tank(1, 1, Direction.UP));
        hero.down();
        hero.down();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");


        hero.left();
        hero.left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        Tank other = tank(5, 5, Direction.UP);
        game.addAi(other);

        other.right();
        other.right();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ˃☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        other.up();
        other.up();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletHasSameDirectionAsTank() {
        givenGame(tank(1, 1, Direction.UP));
        hero.act();
        game.tick();

        Tank realTank = (Tank) hero;
        assertEquals(realTank.getBullets().iterator().next().getDirection(), realTank.getDirection());
    }

    @Test
    public void shouldBulletGoInertiaWhenTankChangeDirection() {
        givenGame(tank(3, 1, Direction.UP));
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ► ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallUp() {
        givenGame(tank(1, 1, Direction.UP));
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallRight() {
        givenGame(tank(1, 1, Direction.RIGHT));
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallLeft() {
        givenGame(tank(5, 5, Direction.LEFT));
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•   ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallDown() {
        givenGame(tank(5, 5, Direction.DOWN));
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - снизу
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 5));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - слева
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft() {
        givenGame(tank(1, 1, Direction.RIGHT), new Wall(5, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - справа
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight() {
        givenGame(tank(5, 1, Direction.LEFT), new Wall(1, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp() {
        givenGame(tank(1, 5, Direction.DOWN), new Wall(1, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - снизу но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_overWall() {
        givenGame(tank(1, 2, Direction.UP), new Wall(1, 5));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - слева но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_overWall() {
        givenGame(tank(2, 1, Direction.RIGHT), new Wall(5, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - справа но через стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_overWall() {
        givenGame(tank(4, 1, Direction.LEFT), new Wall(1, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_overWall() {
        givenGame(tank(1, 4, Direction.DOWN), new Wall(1, 1));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 5), new Wall(1, 4));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_whenTwoWalls() {
        givenGame(tank(1, 1, Direction.RIGHT), new Wall(5, 1), new Wall(4, 1));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_whenTwoWalls() {
        givenGame(tank(5, 1, Direction.LEFT), new Wall(1, 1), new Wall(2, 1));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_whenTwoWalls() {
        givenGame(tank(5, 5, Direction.DOWN), new Wall(5, 1), new Wall(5, 2));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я иду а спереди стена, то я не могу двигаться дальше
    @Test
    public void shouldDoNotMove_whenWallTaWay_goDownOrLeft() {
        givenGame(tank(3, 3, Direction.DOWN),
                new Wall(3, 4), new Wall(4, 3),
                new Wall(3, 2), new Wall(2, 3));

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▲╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬◄╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▼╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣►╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▲╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣◄╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╨  ☼\n" +
                "☼ ╡▼╞ ☼\n" +
                "☼  ╥  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void removeAllNear() {
        hero.up();
        game.tick();
        hero.act();
        game.tick();

        hero.left();
        game.tick();
        hero.act();
        game.tick();

        hero.right();
        game.tick();
        hero.act();
        game.tick();

        hero.down();
        game.tick();
        hero.act();
        game.tick();
    }


    // если я стреляю дважды, то выпускается два снаряда
    // при этом я проверяю, что они уничтожаются в порядке очереди
    @Test
    public void shouldShotWithSeveralBullets_whenHittingTheWallDown() {
        size = 9;
        givenGame(tank(7, 7, Direction.DOWN), new Wall(7, 1), new Wall(7, 2));
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╦☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╥☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╦☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╥☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // стоит проверить, как будут себя вести полуразрушенные конструкции, если их растреливать со всех других сторон
    @Test
    public void shouldDestroyFromUpAndDownTwice() {
        givenGame(tank(3, 4, Direction.DOWN), new Wall(3, 3));

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        hero.down();
        game.tick();

        hero.down();
        game.tick();

        hero.left();
        game.tick();

        hero.up();
        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ─  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // стоять, если спереди другой танк
    @Test
    public void shouldStopWhenBeforeOtherTank() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank1.down();
        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // геймовер, если убили не бот-танк
    @Test
    public void shouldDieWhenOtherTankKillMe() {
        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(1, 2, Direction.DOWN);
        Tank tank3 = tank(1, 3, Direction.DOWN);
        givenGameWith(tank1, tank2, tank3);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼˅    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertAlive(tank1);
        assertAlive(tank2);
        assertAlive(tank3);

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertAlive(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank3.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertGameOver(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);

        game.tick();

        assertGameOver(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);
    }

    private void assertGameOver(Tank tank) {
        assertFalse(tank.isAlive());
    }

    private void assertAlive(Tank tank) {
        assertTrue(tank.isAlive());
    }

    // стоять, если меня убили
    @Test
    public void shouldStopWhenKill() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNoConcurrentException() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.act();
        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet() {
        size = 9;
        Tank tank1 = tank(1, 7, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet2() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Ignore
    @Test
    public void shouldRemoveAIWhenKillIt() {
        // TODO givenGameWithAI(tank(1, 1, Direction.UP), tank(1, 5, Direction.UP), tank(5, 1, Direction.UP));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.right();

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼   •˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼    Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        when(dice.next(anyInt())).thenReturn(3, 3);
        game.tick();

        assertW("☼☼☼☼☼☼☼\n" + // TODO разобраться почему тут скачет ассерт
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void assertW(String expected) {
        Printer<String> printer = getPrinter();
        assertEquals(expected, printer.print().replaceAll("[«¿»?•]", " "));
    }

    @Test
    public void shouldRegenerateDestroyedWall() {
        shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls();

        hero.act();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 7; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCantGoIfWallAtWay() {
        givenGame(tank(1, 1, Direction.UP));
        game.addBorder(new Border(1, 2));
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletCantGoIfWallAtWay() {
        givenGame(tank(1, 1, Direction.UP));
        game.addBorder(new Border(1, 2));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        hero.act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►  •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyOneBulletPerTick() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 2), new Wall(1, 3));
        hero.act();
        hero.act();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.act();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanFireIfAtWayEnemyBullet() {
        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(1, 5, Direction.DOWN);
        givenGameWith(tank1, tank2);

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfDestroyWall() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 2), new Wall(1, 3));
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldWallCantRegenerateOnTank() {
        shouldTankCanGoIfDestroyWall();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        for (int i = 2; i <= Wall.REGENERATE_TIME; i++) {
            assertD("☼☼☼☼☼☼☼\n" +
                    "☼     ☼\n" +
                    "☼     ☼\n" +
                    "☼╬    ☼\n" +
                    "☼ ►   ☼\n" +
                    "☼     ☼\n" +
                    "☼☼☼☼☼☼☼\n");

            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldWallCantRegenerateOnBullet() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 3));
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNTicksPerBullet() {
        ticksPerBullets = 4;
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 3));

        hero.act();
        game.tick();

        String field =
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n";
        assertD(field);

        for (int i = 1; i < ticksPerBullets; i++) {
            hero.act();
            game.tick();

            assertD(field);
        }

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");


    }

    @Ignore
    @Test
    public void shouldNewAIWhenKillOther() {
        givenGameWithAI(tank(1, 1, Direction.UP), 
                tank(1, 5, Direction.UP), 
                tank(5, 1, Direction.LEFT));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˂☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.right();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •˂☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        // TODO разобраться почему тут скачет тест
        assertEquals(2, getPrinter().print().replaceAll("[Ѡ ►☼\n•]", "").length());
    }

    @Test
    public void shouldOnlyRotateIfNoBarrier() {
        givenGame(tank(1, 1, Direction.UP), new Wall(3, 1));
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfBarrier() {
        givenGame(tank(2, 1, Direction.UP), new Wall(3, 1));
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankOnWall() {
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 2));
        Tank tank2 = tank(1, 3, Direction.DOWN);
        initPlayer(game, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.up();  // команда поигнорится потому что вначале ходят все танки, а потом летят все снаряды
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldDieWhenMoveOnBullet() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet2() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 2, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet3() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 3, Direction.UP);
        givenGameWith(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

    }

    // если стенка недорушенная, снаряд летит, и ресетнули игру, то все конструкции восстанавливаются
    @Test
    public void shouldRemoveBulletsAndResetWalls_whenReset() {
        size = 11;
        ticksPerBullets = 3;
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 9), new Wall(1, 8));

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act(); // не выйдет
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╨        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // допустим за игру он прибил 5 танков
        Player player = players.iterator().next();
        player.setKilled(5);

        // when
        game.clearScore();

        // смогу стрельнуть, пушка ресетнется
        hero.act();
        game.tick();

        // then
        // но после рисета это поле чистится
        assertEquals(0, player.score());

        // и стенки тоже ресетнулись
        // и снаряд полетел
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // первый выстрел иногда получается сделать дважды
    @Test
    public void shouldCantFireTwice() {
        size = 11;
        ticksPerBullets = 4;
        givenGame(tank(1, 1, Direction.UP));

        game.clearScore();

        game.tick(); // внутри там тикает так же gun, но первого выстрела еще небыло
        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // 1. Кусты
    @Test
    public void shouldBeWallTree_whenGameCreated() {
		givenGame(tank(1, 1, Direction.UP));
        game.addTree(new Tree(3, 3));

        assertEquals(1, game.getTrees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▒  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeWallTwoTree_whenGameCreated() {
		givenGame(tank(1, 1, Direction.UP));
        game.addTree(new Tree(3, 3));
        game.addTree(new Tree(5, 1));

        assertEquals(2, game.getTrees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▒  ☼\n" +
                "☼     ☼\n" +
                "☼▲   ▒☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // 1.1) При выстреле пуля должна пролетать сквозь кусты
    @Test
    public void shouldBulletFlyUnderTree_right() {
        size = 11;

		givenGame(tank(1, 1, Direction.UP));
		game.addTree(new Tree(6, 1));

        hero.right();
        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►•  ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►  •▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒•  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒  •☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWallUnderTree_whenHittingTheWallUp_whenTwoWalls() {
        size = 7;
        givenGame(tank(1, 1, Direction.UP), new Wall(1, 5), new Wall(1, 4));
		game.addTree(new Tree(1, 2));

        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private List<Wall> walls(Wall... walls) {
        return new LinkedList<>(Arrays.asList(walls));
    }

    // Когда пуля и дерево находятся в одной координате когда отработывает метод tick()
    @Test
    public void shouldBulletFlyUnderTwoTree_up() {
        size = 11;
        givenGame(tank(5, 1, Direction.UP));
        game.addTree(new Tree(5, 5));
        game.addTree(new Tree(5, 6));

        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // 1.2) кусты - когда игрок заходит под них, там видно кусты и больше никакого движения
    @Test
    public void shouldTankMove_underTree() {
        size = 7;
        givenGame(tank(1, 1, Direction.UP));
        game.addTree(new Tree(1, 3));
        game.addTree(new Tree(1, 4));

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒►   ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒ ►  ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly_shouldTankMoveUnderTree() {
        size = 11;

        givenGame(tank(9, 1, Direction.UP));

        game.addTree(new Tree(9, 5));
        game.addTree(new Tree(9, 6));
        game.addTree(new Tree(9, 3));

        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼        ▲☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        hero.up();
        game.tick();

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // 1.3) так же не видно врагов под кустами
    @Test
    public void shouldOtherTankMove_underTree() {
		size = 11;

		Tank tank1 = tank(1, 1, Direction.UP);
		Tank tank2 = tank(1, 9, Direction.DOWN);
        givenGameWith(tank1, tank2);

        game.addTree(new Tree(1, 6));
        game.addTree(new Tree(1, 7));

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼˅        ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

        tank2.down();
        game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼˅        ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		tank2.down();
		game.tick();

		tank2.down();
		game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		tank2.down();
		game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼˅        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // 1.4) под кустами не видно так же и ботов белых
    @Test
    public void shouldAITankMove_underTree() {
        size = 11;

        Tank hero = tank(1, 1, Direction.UP);
        givenGameWithAI(hero, pt(1, 9));
        Tank aiTank = game.getAiTanks().get(0);

        game.addTree(new Tree(1, 6));
        game.addTree(new Tree(1, 7));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼¿        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankUnderTree() {
        size = 11;

        Tank tank1 = tank(1, 3, Direction.UP);
        Tank tank2 = tank(1, 9, Direction.DOWN);
        givenGameWith(tank1, tank2);

        game.addTree(new Tree(1, 4));
        game.addTree(new Tree(1, 5));
        game.addTree(new Tree(1, 6));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        tank1.up();
        game.tick();// герой запрятался в кустах

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertTrue(tank1.isAlive());
        game.tick();// герой должен погибнуть

        assertFalse(tank1.isAlive());
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTwoTankCanPassThroughEachOtherUnderTree() {
        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(1, 4, Direction.DOWN);
        givenGameWith(tank1, tank2);

        game.addTree(new Tree(1, 2));
        game.addTree(new Tree(1, 3));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.down();
        tank1.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.down();
        tank1.up();
        game.tick();

        tank2.down();
        game.tick();

        tank1.up();
        // Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.right();
        tank1.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒˃   ☼\n" +
                "☼▒►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// 2. Лёд
    @Test
    public void shouldBeWallIce_whenGameCreated() {
        givenGame(tank(1, 1, Direction.UP));

        game.addIce(new Ice(3, 3));

        assertEquals(1, game.getIce().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  █  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // 2.1) когда герой двигается по льду, происходит скольжение
    // (он проскальзывает одну команду).
    // Если только заезжаем - то сразу же начинается занос,
    // то есть запоминается команда которой заезжали на лед
    // Если съезжаем на землю, то любой занос прекращается тут же
    @Test
    public void shouldTankMoveUP_onIce_afterBeforeGround() {
        size = 11;

        givenGame(tank(5, 2, Direction.UP));

        game.addIce(new Ice(5, 3));
        game.addIce(new Ice(5, 4));
        game.addIce(new Ice(5, 5));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // находимся на льду
        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // двигаемся дальше в направлении up()
        // UP -> UP (выполнилась)
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выехали со льда
        // двигается дальше в направлении up()
        // UP -> UP (выполнилась)
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankMoveLeftThenUpThenDown_onIce() {
        size = 11;

        givenGame(tank(5, 2, Direction.UP));

        game.addIce(new Ice(5, 3));
        game.addIce(new Ice(5, 4));
        game.addIce(new Ice(5, 5));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> UP (скольжение)
        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN (выполнилась)
        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> DOWN (скольжение)
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //2.2) также когда на нем двигается враг он проскальзывает команду на два тика
    @Test
    public void shouldOtherTankMoveLeftThenUpThenDown_onIce() {
        size = 11;

        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(5, 6, Direction.DOWN);
        givenGameWith(tank1, tank2);

        game.addIce(new Ice(5, 3));
        game.addIce(new Ice(5, 4));
        game.addIce(new Ice(5, 5));

        // then
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // враг заежает на лёд
        tank2.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> DOWN(скольжение)
        tank2.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP (выполнилась)
        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> UP (скольжение)
        // сьезд со льда
        tank2.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //2.3) также когда на нем двигается бот он проскальзывает команду на два тика

    //3. Река
    @Test
    public void shouldBeWallWater_whenGameCreated() {
        givenGame(tank(1, 1, Direction.UP));

        game.addRiver(new River(3, 3));

        assertEquals(1, game.getRivers().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// 3.1) река - через нее герою нельзя пройти. но можно стрелять
	@Test
	public void shouldTankCanGoIfRiverAtWay() {
        givenGame(tank(1, 1, Direction.UP));
        game.addRiver(new River(1, 2));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	@Test
	public void shouldBulletCanGoIfRiverAtWay() {
        givenGame(tank(1, 1, Direction.UP));
        game.addRiver(new River(1, 2));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero.up();
		game.tick();

		hero.act();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼•    ☼\n" +
				"☼▓    ☼\n" +
				"☼▲    ☼\n" +
				"☼☼☼☼☼☼☼\n");

		hero.right();
		hero.act();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼•    ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼▓    ☼\n" +
				"☼ ►•  ☼\n" +
				"☼☼☼☼☼☼☼\n");

		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼▓    ☼\n" +
				"☼ ►  •☼\n" +
				"☼☼☼☼☼☼☼\n");
	}

    @Test
    public void shouldDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenGame(tank(3, 3, Direction.UP));
        riverAround();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓▲▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓►▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓▲▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓◄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero.down();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼  ▓  ☼\n" +
				"☼ ▓▼▓ ☼\n" +
				"☼  ▓  ☼\n" +
				"☼     ☼\n" +
				"☼☼☼☼☼☼☼\n");
    }

    // 3.2) река - через нее врагу нельзя пройти. но можно стрелять
    @Test
    public void shouldOtherTankBullet_canGoIfRiverAtWay() {
        Tank tank1 = tank(3, 2, Direction.UP);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWith(tank1, tank2);

        game.addRiver(new River(1, 2));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.right();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ ˃•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ ˃  •☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        Tank tank1 = tank(5, 1, Direction.UP);
        Tank tank2 = tank(3, 3, Direction.UP);
        givenGameWith(tank1, tank2);

        riverAround();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˃▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˂▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˅▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // 3.3) река - через нее боту нельзя пройти. но можно стрелять
    @Test
    public void shouldAITankBullet_canGoIfRiverAtWay() {
        Tank hero = tank(3, 2, Direction.UP);
        givenGameWithAI(hero, pt(1, 1));
        Tank aiTank = game.getAiTanks().get(0);
        aiTank.direction = Direction.UP;

        game.addRiver(new River(1, 2));

        // then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        aiTank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.right();
        aiTank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ »•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼  ◘ •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼   » ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAITankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        Tank hero = tank(5, 1, Direction.UP);
        givenGameWithAI(hero, pt(3, 3));
        Tank aiTank = game.getAiTanks().get(0);
        riverAround();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓◘▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓?▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼•▓«▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓¿▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void riverAround() {
        game.addRiver(new River(2, 3));
        game.addRiver(new River(4, 3));
        game.addRiver(new River(3, 2));
        game.addRiver(new River(3, 4));
    }

    // TODO 4. Добовляем бота
    // TODO    4.1) добавляем бота, который спаунится каждые N ходов (задается в сеттингах),
    // TODO         который цветной и его убить можно только за M выстрелов (тоже сеттинги)
    // TODO    4.2) во время смерти такого AI вываливается приз
    // создаем АИтанк с призами
    @Test
    public void shouldCreatedAiPrize() {
        size = 9;
        hitKillsAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(7, 7));
        Tank aiTank = game.getAiTanks().get(0);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 tanks", getPrizesCount());
    }

    // У АИтанка с призами после 4-го хода должен смениться Element
    @Test
    public void shouldSwapElementAfterFourTicks() {
        size = 9;
        hitKillsAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(7, 7));
        Tank aiTank = game.getAiTanks().get(0);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    •« ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼     ? ☼\n" +
                "☼  •    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунится сразу 2 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenTwoAi() {
        size = 9;
        spawnAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(2, 7), pt(7, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("1 prizes with 3 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3 и спаунится сразу 3 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenThreeAi() {
        size = 9;
        spawnAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(2, 7), pt(5, 7), pt(7, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("1 prizes with 4 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунятся сразу 6 АИтанков, то должно быть 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrizeWhenSixAi() {
        size = 9;
        spawnAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(2, 7), pt(3, 7), pt(4, 7), pt(5, 7), pt(6, 7), pt(7, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿◘¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("2 prizes with 7 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а 3 АИтанка спаунятся по 1-му за каждый ход,
    // то АИтанк с призами спаунится после 2-го хода
    // так же проверяем что призовой танк меняет свой символ каждые 4 тика
    @Test
    public void shouldSpawnAiPrize_whenAddOneByOneAI() {
        size = 9;
        spawnAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(2, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(5, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿     ☼\n" +
                "☼ •     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(6, 7));
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿  •  ☼\n" +
                "☼       ☼\n" +
                "☼ •     ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 4 tanks", getPrizesCount());

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿• ☼\n" +
                "☼ ◘     ☼\n" +
                "☼    •  ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿   • ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼▲¿     ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // в АИтанк с призами надо попасть 3 раза, чтобы убить
    @Test
    public void shouldKillAiPrizeInThreeHits() {
        size = 7;
        hitKillsAiPrize = v(3);
        Tank tank = tank(1, 1, Direction.UP);
        givenGameWithAI(tank, pt(1, 5));
        Tank aiTank = game.getAiTanks().get(0);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrizeInPointKilledAiPrize() {
        size = 7;
        givenGameBeforeDropPrize(pt(1,5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrizeInFreePoint() {
        size = 7;
        givenGameBeforeDropPrize(pt(4,5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼   1 ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNotDropPrizeInPointPlayerTank() {
        size = 7;
        givenGameBeforeDropPrize(pt(1,1));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNotDropPrizeInPointWall() {
        size = 7;
        givenGameBeforeDropPrize(pt(3,3));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNotDropPrizeInPointField() {
        size = 7;
        givenGameBeforeDropPrize(pt(0,2));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // TODO 5. Добавляем приз
    // TODO    5.1) добавляем приз неуязвимости, он работает L тиков (сеттинги) и теряется после
    // TODO    5.2) если в тебя выстрелят во время действия этого приза - ты остаешься жить
    // TODO 6. Ддобавляется приз пульки которые разбивают бетонную стену
    // TODO 7. Рендомная борда (тут надо подглядеть как в icancode подобное устроено) и сделать так же

}
