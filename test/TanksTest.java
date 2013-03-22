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
    private Tanks game = new Tanks(3);
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
    private Field field = game.getField();
    @Test
    public void shouldFieldHasSize() {
        assertEquals(true, field.getSize() != 0);
    }

    //С: при создании игры должно создаваться поле указанного размера

    @Test
    public void shouldFieldHasSize3WhenGameCreated() {
        Tanks game = new Tanks(3);
        assertEquals(3, game.getField().getSize());
    }

    //С: Поле должно быть квадратным. Как мы уже говорили на
    // треннинге, это можно решить даже без написания теста -
    // мы договариваемся изначально, что поле квадратное.

    //С: Комментарии занимают оч много времени, но мы никуда не спешим.

    //С: Поле должно отображаться на экране.
    @Test
    public void shouldFieldBeDrawable() {
        assertEquals("*********", game.drawField());
    }

    //Е: Надо привязаться к интерфейсу. потому вывод поля зависеть должен
    //от размера, а не быть константой
    @Test
    public void shouldDrawOnScreen() {
        Tanks game = new Tanks(4);
        assertEquals("****************",game.drawField());
    }
    //Тест поломался. Сейчас появилось больше конкретики:
    //класс Field содержит поля, в которых забиты символы,
    // поле, в котором забиты линии и метод инициализации,
    //который можно всунуть в конструктор

    //Коллективный разум решил, что метод drawFIeld() будет возвращать
    // не просто поле, а поле с бордером, а само поле возвращает лишь землю:)
}
