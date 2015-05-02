package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomCommandTest {

    @Test
    public void shouldGenerate() {
        // given
        Dice dice = mock(Dice.class);
        RandomCommand generator = new RandomCommand(dice);

        when(dice.next(100)).thenReturn(6*3);
        when(dice.next(6*3)).thenReturn(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);

        //when
        String next = generator.next();

        //then
        assertEquals("BB2B'DD2D'FF2F'LL2L'RR2R'UU2U'", next);
    }
}
