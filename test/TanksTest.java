import com.javatrainee.tanks.Tanks;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TanksTest {
    // Итак, начнем с того, что у нас есть поле. Но его пока нет:)
    // 1. Уясним для начала, что игра будет называться "Tanks"
    // 2. Игра должа создаваться
    // 3. Должно создаваться поле.
    // 4. Поле должно будет иметь размер
    // .... Пока что хватит, не будем тратить время на раздумиЯ.
   @Test
    public void shouldGame_beCreating() {
       Tanks game = new Tanks();
       assertNotNull(game);
   }
}
