package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.rubicscube.client.Board;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sanja on 23.02.14.
 */
public class BoardTest {

    @Test
    public void shouldToString() {
        Board board = (Board) new Board().forString(
                "   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        assertEquals(
                "   WWW      \n" +
                "   WWW      \n" +
                "   WWW      \n" +
                "BBBOOOGGGRRR\n" +
                "BBBOOOGGGRRR\n" +
                "BBBOOOGGGRRR\n" +
                "   YYY      \n" +
                "   YYY      \n" +
                "   YYY      \n", board.toString());
    }
}
