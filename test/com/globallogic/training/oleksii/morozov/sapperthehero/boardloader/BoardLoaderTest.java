package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoader;
import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoaderImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

public class BoardLoaderTest {
	private static final String FAKE_FILE_NAME = "boards/board1asda";
	private static final String REAL_FILE_NAME = "boards/board1";
	private BoardLoader boardLoader;

	@Before
	public void setUp() {
		boardLoader = new BoardLoaderImpl();
		boardLoader.readFile(REAL_FILE_NAME);
	}

	@Test
	public void shouldBoardLoader() {
		assertNotNull(boardLoader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_whenFakePathToFile() {
		boardLoader.readFile(FAKE_FILE_NAME);
	}

	@Test
	public void shouldLoadBoardFromFile_whenFileIsReaded() {
		Board board = boardLoader.getBoard();
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

	// TODO
	@Test
	public void shouldMineCoordinatesFromFile() {
		assertEquals(8, 8);
	}

}