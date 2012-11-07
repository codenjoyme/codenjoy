package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.boardsaver.BoardSaver;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

public class BoardSaverTest {
	
	public BoardSaver boardSaver;

	@Before
	public void setUp() {
		
	}
	@Test
	public void shouldSaveBoardToFile() {
		Board board = mock(Board.class);
		Sapper sapper = mock(Sapper.class);
		when(board.getSize()).thenReturn(10);
		when(sapper.getMineDetectorCharge()).thenReturn(5);
		when(board.getMines()).thenReturn(
				Arrays.asList(new Mine(0, 1), new Mine(1, 0)));

		int gameId = 2;
		boardSaver.saveBoard(board, gameId);
		

		verify(board);
		assertEquals(4, board.getMinesCount());
	}
}