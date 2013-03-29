import com.javatrainee.tanks.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TanksTest {
    // Е: Итак, начнем с того, что у нас есть поле. Но его пока нет:)
    // 1. Уясним для начала, что игра будет называться "Tanks"
    // 2. Игра должа создаваться
    // 3. Должно создаваться поле.
    // 4. Поле должно будет иметь размер
    // .... Пока что хватит, не будем тратить время на раздумиЯ.
   // private final int FIELD_SIZE = 13;
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
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
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
        Printer printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X■************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        field.setTank(new Tank(1,1));
        assertNotNull(field.getTank());
    }

    @Test
    public void shouldTankHasSize1WhenGameCreated() {
        field.setTank(new Tank(1,1));
        Tank someTank = field.getTank();
        assertEquals(1, someTank.getSize());
    }

    @Test
    public void shouldDrawTankOnTheField() {
        field.setTank(new Tank(1,1));
        Printer printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X*************X" +
                "X*▲***********X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
    }
    @Test
    public void shouldTankMove() {
        Tank someTank =  new Tank(1,1);
        field.setTank(someTank);
        someTank.moveUp();
        field.setTank(someTank);
        Printer printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X*▲***********X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
        someTank.moveDown();
        field.setTank(someTank);
        printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*▼***********X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
        someTank.moveRight();
        field.setTank(someTank);
        printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X**►**********X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
        someTank.moveLeft();
        field.setTank(someTank);
        printer = new Printer(field);
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*◄***********X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
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
        tank.moveDown();
        assertEquals(Direction.DOWN, tank.getDirection());
        tank.moveRight();
        assertEquals(Direction.RIGHT, tank.getDirection());
        tank.moveUp();
        assertEquals(Direction.UP, tank.getDirection());
        tank.moveLeft();
        assertEquals(Direction.LEFT, tank.getDirection());
    }

    @Test
    public void shouldPrinterDisplayedDirection() {
        tank.moveDown();
        assertEquals(
                        "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*************X" +
                        "X*▼***********X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldTankStayAtPreviousPositionWhenIsNearBorder() {
        tank.moveUp();
        tank.moveUp();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*▲***********X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
        tank.moveLeft();
        tank.moveLeft();
        tank.moveUp();
        tank.moveLeft();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X◄************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
        Tank someTank = new Tank(12, 12);
        field.setTank(someTank);
        someTank.moveRight();
        someTank.moveDown();
        someTank.moveDown();
        someTank.moveRight();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X************►X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldTankCanFire() {
        tank.fire();
        Bullet bullet = tank.getBullet();
        assertNotNull(bullet);
    }

    @Test
    public void shouldBulletHasSameDirectionAsTank() {
        tank.fire();
        Bullet bullet = tank.getBullet();
        assertEquals(tank.getDirection(), bullet.getDirection());
    }

    @Test
    public void shouldBulletBeOnFieldWhenTankFireAndMoveRight() {
        Tank someTank = new Tank(6, 12);
        field.setTank(someTank);
        someTank.fire();
        someTank.getBullet().move();
        someTank.moveRight();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X******•******X" +
                        "X*************X" +
                        "X*******►*****X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldBulletMoveAfterEachTact() {
        Tank someTank = new Tank(6, 12);
        field.setTank(someTank);
        someTank.fire();
        game.tact();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X*************X" +
                "X******•******X" +
                "X*************X" +
                "X******▲******X" +
                "XXXXXXXXXXXXXXX", printer.drawField());
        game.tact();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X******•******X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X******▲******X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldBulletDisappearWhenHittingTheWall() {
        Tank someTank = new Tank(6, 3);
        field.setTank(someTank);
        someTank.fire();
        game.tact();
        game.tact();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X******▲******X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());
    }

    @Test
    public void shouldBeDO_NOTHINGCommandByDefault() {
        game.getJoystick().setCommandByPressedKey("");
        Command userCommand = game.getJoystick().getCommand();
        assertEquals(Command.DO_NOTHING, userCommand);
    }

    @Test
    public void shouldFormCommandsByPressedKeys() {
        game.getJoystick().setCommandByPressedKey("w");
        Command userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_UP, userCommand);
        game.getJoystick().setCommandByPressedKey("s");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_DOWN, userCommand);
        game.getJoystick().setCommandByPressedKey("a");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_LEFT, userCommand);
        game.getJoystick().setCommandByPressedKey("d");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_RIGHT, userCommand);
    }

    @Test
    public void shouldFormCommandsByPressedKeysFromConsole() {
        game.getJoystick().setCommandByPressedKey(new Scanner(System.in).nextLine());
        Command userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_UP, userCommand);
        game.getJoystick().setCommandByPressedKey("s");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_DOWN, userCommand);
        game.getJoystick().setCommandByPressedKey("a");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_LEFT, userCommand);
        game.getJoystick().setCommandByPressedKey("d");
        userCommand = game.getJoystick().getCommand();
        assertEquals(Command.MOVE_RIGHT, userCommand);
    }
    /*
    @Test
    public void shouldTankMoveUpWhenPressedUpArrowOnKeyboard() {
        Tank someTank = new Tank(6, 3);
        field.setTank(someTank);
        game.action();
        assertEquals(
                "XXXXXXXXXXXXXXX" +
                        "X*************X" +
                        "X*************X" +
                        "X******▲******X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "X*************X" +
                        "XXXXXXXXXXXXXXX", printer.drawField());

    }
    */

}
