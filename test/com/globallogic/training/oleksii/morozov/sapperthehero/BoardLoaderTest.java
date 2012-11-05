package com.globallogic.training.oleksii.morozov.sapperthehero;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoader;
import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoaderImpl;

public class BoardLoaderTest {
	private static final String FAKE_FILE_NAME = "boards/board1asda";
	private static final String REAL_FILE_NAME = "boards/board1";
	private BoardLoader boardloader;
	

	@Before
	public void setUp() {
		boardloader = new BoardLoaderImpl();
	}

	@Test
	public void shouldBoardLoader() {
		assertNotNull(boardloader);
	}

	@Test(expected = FileNotFoundException.class)
	public void shouldFail_whenFakePathToFile() throws FileNotFoundException, IOException {
		boardloader.readFile(FAKE_FILE_NAME);
	}
	
	@Test
	public void shouldReadFile() throws FileNotFoundException, IOException {
		assertNotNull(boardloader.readFile(REAL_FILE_NAME));
	}
	
}