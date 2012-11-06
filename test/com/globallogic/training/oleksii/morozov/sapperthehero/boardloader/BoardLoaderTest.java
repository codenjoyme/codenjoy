package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoader;
import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoaderImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

public class BoardLoaderTest {
	private static final int FAKE_FILE_NAME = 2;
	private static final int REAL_FILE_NAME = 1;
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

}