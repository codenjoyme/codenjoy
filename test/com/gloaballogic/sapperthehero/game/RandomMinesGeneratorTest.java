package com.gloaballogic.sapperthehero.game;

import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Mine;
import com.globallogic.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.sapperthehero.game.minegenerator.impl.RandomMinesGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class RandomMinesGeneratorTest {


    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            List<Mine> minesAnother = generate();
            if (mines.equals(minesAnother)) {
                fail("Встретилась ситуакия, когда на двух разных " +
                        "полях мины появились в одном месте");
            }
        }
    }

    private List<Mine> generate() {
        return new RandomMinesGenerator().get(10, new MockBoard());
    }

    private class MockBoard extends Board {
        public MockBoard() {
            super(16, 0, 1, new MinesGenerator() {
                @Override
                public List<Mine> get(int count, Board board) {
                    return Arrays.asList();
                }
            });
        }
    }
}
