import com.javatrainee.tanks.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TanksTest {
    // Е: Итак, начнем с того, что у нас есть поле. Но его пока нет:)
    // 1. Уясним для начала, что игра будет называться "Tanks"
    // 2. Игра должа создаваться
    // 3. Должно создаваться поле.
    // 4. Поле должно будет иметь размер
    // .... Пока что хватит, не будем тратить время на раздумиЯ.
    private final int FIELD_SIZE = 13;
    private Tanks game;
    private Field field;
    private Printer printer;

    @Before
    public void startGame() {
        game = new Tanks(FIELD_SIZE);
        field = game.getField();
        printer = new Printer(field);
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
                "X*T***********X" +
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
                "X*T***********X" +
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
                        "X*T***********X" +
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
                        "X**T**********X" +
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
                        "X*T***********X" +
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
}
