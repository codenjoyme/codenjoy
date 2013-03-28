import com.javatrainee.tanks.*;
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
    private Tanks game = new Tanks(3);

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
        Field field = new Field(3);
        assertEquals(field.getClass(), game.getField().getClass());
    }
    private Field field = game.getField();

    @Test
    public void shouldFieldHasSize() {
        assertEquals(true, field.getSize() != 0);
    }

    @Test
    public void shouldFieldHasSize3WhenGameCreated() {
        Tanks game = new Tanks(3);
        assertEquals(3, game.getField().getSize());
    }

    @Test
    public void shouldFieldBeDrawable() {
        Printer printer = new Printer(field);
        assertNotNull(printer.drawField());
    }

    @Test
    public void shouldDrawField() {
        Tanks someGame = new Tanks(2);
        Field someField = someGame.getField();
        Printer printer = new Printer(someField);
        assertEquals("XXXXX**XX**XXXXX", printer.drawField());
    }

    @Test
    public void shouldDraw36ElementsOnScreen() {
        Tanks someGame = new Tanks(4);
        Field someField = someGame.getField();
        Printer printer = new Printer(someField);
        assertEquals("XXXXXX" +
                            "X****X" +
                            "X****X" +
                            "X****X" +
                            "X****X" +
                            "XXXXXX",printer.drawField());
    }

    @Test
    public void shouldBeConstruction_WhenGameCreated() {
        field.setConstruction(new Construction(0, 0));
        assertNotNull(field.getConstruction());
    }

    @Test
    public void shouldBeConstructionOnFieldAt0_0() {
        Tanks someGame = new Tanks(4);
        Field someField = someGame.getField();
        someField.setConstruction(new Construction(0, 0));
        Printer printer = new Printer(someField);
        assertEquals("XXXXXX" +
                "X■***X" +
                "X****X" +
                "X****X" +
                "X****X" +
                "XXXXXX", printer.drawField());
    }

    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        Tank someTank = field.getTank();
        assertNotNull(someTank);
    }
    /*

    @Test
    public void shouldBeTankWhenGameCreated() {
        assertNotNull(new Tank(0,0));
    }

    @Test
    public void shouldHasSize1WhenGameCreated() {
        Tank tank = new Tank(0,0);
        assertEquals(1, tank.getSize());
    }

    @Test
    public void shouldTankBeDrawn() {
        assertNotNull(game.drawTank());
    }

    @Test
    public void shouldDrawTankOnTheField() {
        Tanks someGame = new Tanks(4);
        Field someField = someGame.getField();
        Tank someTank = new Tank(0, 2);
        someField = someTank.putOnField(someField);
        assertEquals("**T*"
                        + "****"
                        + "****"
                        + "****", someField.getFieldAsLine());
    }     */
}
