package com.codenjoy.dojo.battlecity.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TanksTest {
    private Tanks game;
    private Field field;
    private Printer printer;
    private Tank tank;

    @Before
    public void startGame() {
        game = new Tanks();
        field = game.getField();
        printer = new Printer(field);
        tank = new Tank(1, 1);
        field.setTank(tank);
    }

    @Test
    public void shouldGame_beCreating() {
       assertNotNull(game);
   }

    @Test
    public void shouldBeFieldWhenGameStarted() {
        assertNotNull(game.getField());
    }

    @Test
    public void shouldFieldBeOfTypeField() {
        assertEquals(field.getClass(), game.getField().getClass());
    }

    @Test
    public void shouldFieldHasSize() {
        assertEquals(true, field.getSize() != 0);
    }

    @Test
    public void shouldFieldHasSize13WhenGameCreated() {
        assertEquals(13, game.getField().getSize());
    }

    @Test
    public void shouldFieldBeDrawable() {
        assertNotNull(printer.drawField());
    }

    @Test
    public void shouldDrawField() {
        field.setTank(null);
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    private void assertDraw(String field) {
        assertEquals(field, printer.drawField());
    }

    @Test
    public void shouldBeConstruction_WhenGameCreated() {
        field.setConstruction(new Construction(0, 0));
        assertNotNull(field.getConstruction());
    }

    @Test
    public void shouldBeConstructionOnFieldAt0_0() {
        field.setTank(null);
        field.setConstruction(new Construction(0, 0));
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X■************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
                
    }

    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        assertNotNull(field.getTank());
    }

    @Test
    public void shouldTankHasSize1WhenGameCreated() {
        assertEquals(1, tank.getSize());
    }

    @Test
    public void shouldDrawTankOnTheField() {
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*▲***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
    }
    @Test
    public void shouldTankMove() {
        field.setTank(tank);
        tank.up();
        field.setTank(tank);
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*▲***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
        tank.down();
        field.setTank(tank);
        printer = new Printer(field);
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*▼***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
        tank.right();
        field.setTank(tank);
        printer = new Printer(field);
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X**►**********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
        tank.left();
        field.setTank(tank);
        printer = new Printer(field);
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*◄***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    @Test
    public void shouldTankHasDirection() {
        assertNotNull(tank.getDirection());
    }

    @Test
    public void shouldTankDirectionBeUPWhenGameStarted() {
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    public void shouldTankChangeDirectionWhenMoves() {
        tank.down();
        assertEquals(Direction.DOWN, tank.getDirection());
        tank.right();
        assertEquals(Direction.RIGHT, tank.getDirection());
        tank.up();
        assertEquals(Direction.UP, tank.getDirection());
        tank.left();
        assertEquals(Direction.LEFT, tank.getDirection());
    }

    @Test
    public void shouldPrinterDisplayedDirection() {
        tank.down();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*▼***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    @Test
    public void shouldTankStayAtPreviousPositionWhenIsNearBorder() {
        tank.up();
        tank.up();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*▲***********X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
        tank.left();
        tank.left();
        tank.up();
        tank.left();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X◄************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
        Tank someTank = new Tank(12, 12);
        field.setTank(someTank);
        someTank.right();
        someTank.down();
        someTank.down();
        someTank.right();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X************►X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    @Test
    public void shouldTankCanact() {
        tank.act();
        Bullet bullet = tank.getBullet();
        assertNotNull(bullet);
    }

    @Test
    public void shouldBulletHasSameDirectionAsTank() {
        tank.act();
        Bullet bullet = tank.getBullet();
        assertEquals(tank.getDirection(), bullet.getDirection());
    }

    @Test
    public void shouldBulletBeOnFieldWhenTankactAndright() {
        Tank someTank = new Tank(6, 12);
        field.setTank(someTank);
        someTank.act();
        someTank.getBullet().move();
        someTank.right();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X******•******X\n" +
                "X*************X\n" +
                "X*******►*****X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    @Test
    public void shouldBulletMoveAfterEachtick() {
        Tank someTank = new Tank(6, 12);
        field.setTank(someTank);
        someTank.act();
        game.tick();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X******•******X\n" +
                "X*************X\n" +
                "X******▲******X\n" +
                "XXXXXXXXXXXXXXX\n");
        game.tick();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X******•******X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X******▲******X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

    @Test
    public void shouldBulletDisappearWhenHittingTheWall() {
        Tank someTank = new Tank(6, 3);
        field.setTank(someTank);
        someTank.act();
        game.tick();
        game.tick();
        assertDraw(
                "XXXXXXXXXXXXXXX\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X******▲******X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "X*************X\n" +
                "XXXXXXXXXXXXXXX\n");
    }

}
