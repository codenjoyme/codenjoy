package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.RandomMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;

public class RandomMinesGeneratorTest {

    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            List<Mine> minesAnother = generate();

            assertTrue(!mines.equals(minesAnother));
        }
    }


    private List<Mine> generate() {
        return new RandomMinesGenerator().get(10, new MockBoard());
    }

    private class MockBoard extends BoardImpl {
        public MockBoard() {
            super(16, 0, 1, new MinesGenerator() {
                public List<Mine> get(int count, Board board) {
                    return Arrays.asList();
                }
            });
        }
    }
}
