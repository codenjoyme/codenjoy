import com.javatrainee.tanks.Field;
import com.javatrainee.tanks.Printer;
import com.javatrainee.tanks.Tanks;
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

    /*

    private Construction construction = new Construction();
    @Test
    public void shouldBeConstruction_WhenGameCreated() {
        assertNotNull(construction);
    }
    @Test
    public void shouldConstructionBeDrawable() {
        assertNotNull(construction.drawConstruction());
    }

    @Test
    public void shouldConstructionBeDrawnAsSquare() {
        assertEquals("■", construction.drawConstruction());
    }

    @Test
    public void shouldConstructionBeCreatedInTheMiddleOfTheField() {
        int[] coordinatesOfTheMiddleOfTheField = {field.getSize() / 2, field.getSize() / 2};
        Construction construction = new Construction(field.getSize() / 2, field.getSize() / 2);
        assertEquals(Arrays.toString(coordinatesOfTheMiddleOfTheField), Arrays.toString(construction.getCoordinates()));
    }

   @Test
    public void shouldFieldContainConstruction() {
        Field testField = new Field(3);
        Construction someConstruction = new Construction();
        testField = someConstruction.putOnField(testField);
        assertEquals(true, testField.getFieldAsLine().contains(Symbols.CONSTRUCTION_SYMBOL));
    }

    @Test
    public void shouldConstructionBeAtCoordinates() {
        Tanks someGame = new Tanks(4);
        Construction someConstruction = new Construction(0, 2);
        Field testField = someGame.getField();
        testField = someConstruction.putOnField(testField);
        assertEquals("**■*"
                         +"****"
                         +"****"
                         +"****",testField.getFieldAsLine());
    }

    @Test
    public void shouldDrawConstructionOnTheField() {
        Tanks someGame = new Tanks(3);
        Field someField = someGame.getField();
        Construction someConstruction = new Construction(1, 1);
        someField = someConstruction.putOnField(someField);
        assertEquals("XXXXX"
                +"X***X"
                +"X*■*X"
                +"X***X"
                +"XXXXX", someGame.drawField());
    }

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
