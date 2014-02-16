package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerPrinter;
import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 16.02.14.
 */
public class LevelBuilderTest {

    private Dice dice;
    private LevelBuilder builder;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    @Test
    public void testGetMask() throws Exception {
        builder = new LevelBuilder(4, dice);

        when(dice.next(9)).thenReturn(0,0, 1,2, 3,5, 8,8);

        builder.build();

        assertM("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼???☼???☼?? ☼" +
                "☼???☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼???☼ ??☼???☼" +
                "☼???☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼? ?☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼ ??☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void testGetBoard() throws Exception {
        builder = new LevelBuilder(0, dice);

        builder.build();

        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private void assertM(String expected) {
        assertEquals(LoderunnerTest.injectN(expected), LoderunnerTest.injectN(builder.getMask()));
    }

    private void assertB(String expected) {
        assertEquals(LoderunnerTest.injectN(expected), LoderunnerTest.injectN(builder.getBoard()));
    }
}
