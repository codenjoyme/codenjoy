import com.javatrainee.tanks.Field;
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
    private Tanks game = new Tanks();
    @Test
    public void shouldGame_beCreating() {
       assertNotNull(game);
   }
    //С: мы закоммитились, а это есть хорошо :) Итак, продолжим
    @Test
    public void shouldBeFieldWhenGameStarted() {
        assertNotNull(game.getField());
    }
    //С: поле должно быть полем, а не Object...
    @Test
    public void shouldFieldBeOfTypeField() {
        Field field = new Field(3);
        assertEquals(field.getClass(), game.getField().getClass());
    }

    @Test
    public void shouldFieldHasSize() {
        Field field = game.getField();
        assertEquals(true, field.getSize() != 0);
    }

    //С: при создании игры должно создаваться поле указанного размера

    @Test
    public void shouldFieldHasSize3WhenGameCreated() {
        Field field = game.getField();
        assertEquals(3, field.getSize());
    }
}
