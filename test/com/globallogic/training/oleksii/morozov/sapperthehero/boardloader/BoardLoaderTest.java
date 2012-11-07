package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

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