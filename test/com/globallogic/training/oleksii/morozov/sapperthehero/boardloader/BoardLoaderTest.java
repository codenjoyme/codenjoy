package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

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
	}

	@Test
	public void shouldBoardLoader() {
		assertNotNull(boardLoader);
	}

	@Test(expected = FileNotFoundException.class)
	public void shouldFail_whenFakePathToFile() throws FileNotFoundException,
			IOException {
		boardLoader.readFile(FAKE_FILE_NAME);
	}

	@Test
	public void shouldReadFile() throws FileNotFoundException, IOException {
		assertNotNull(boardLoader.readFile(REAL_FILE_NAME));
	}

	@Test
	public void shouldLoadBoardFromFile_whenFileIsReaded() {
		Board board = boardLoader.getBoard();
		assertNotNull(board);
	}

	@Test
	public void shouldDetectorChargeLoad_whenFile() {
		int charge = boardLoader.getCharge();
		assertEquals(8, charge);
	}
	

}