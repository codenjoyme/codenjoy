package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

public class BoardLoaderTest {
	private static final int FAKE_FILE_NAME = 21231;
	private static final int REAL_FILE_NAME = 1;
	private BoardLoader boardLoader;

	@Before
	public void setUp() throws IOException {
		boardLoader = new BoardLoaderImpl();
		boardLoader.readFromFile(REAL_FILE_NAME);
	}

	@Test
	public void shouldBoardLoader() {
		assertNotNull(boardLoader);
	}

	@Test(expected = FileNotFoundException.class)
	public void shouldFail_whenFakePathToFile() throws IOException {
		BoardLoader boardLoaderFake = new BoardLoaderImpl();
		boardLoaderFake.readFromFile(FAKE_FILE_NAME);
	}

	@Test
	public void shouldLoadBoardFromFile_whenFileIsReaded()
			throws FileNotFoundException, IOException {
		Board board = boardLoader.getBoard(REAL_FILE_NAME);
		assertNotNull(board);
	}

	@Test
	public void shouldBoardSize_whenFileReaded() {
		assertEquals(8, boardLoader.getBoardSize());
	}

	@Test
	public void shouldDetectorCharge_whenFileReaded() {
		assertEquals(9, boardLoader.getCharge());
	}

	@Test
	public void shouldMinesCount_whenFileReaded() {
		assertEquals(4, boardLoader.getMinesCount());
	}

	@Test
	public void shouldSaveBoardToFile() {
		Board board = mock(Board.class);
		Sapper sapper = mock(Sapper.class);
		when(board.getMines()).thenReturn(Arrays.asList(new Mine(0,1), new Mine(1,0)));
		when(board.getSize()).thenReturn(10);
		when(sapper.getMineDetectorCharge()).thenReturn(5);
//		when(board.getSapper().getMineDetectorCharge()).thenReturn(5);
		when(board.getMines()).thenReturn(Arrays.asList(new Mine(0,1), new Mine(1,0)));
		int fileNumber = 2;
		boardLoader.saveBoard(board, fileNumber);
		verify(board);
		assertEquals(4, boardLoader.getMinesCount());
	}
}