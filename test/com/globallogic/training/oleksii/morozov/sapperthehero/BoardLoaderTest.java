package com.globallogic.training.oleksii.morozov.sapperthehero;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoader;
import com.globallogic.training.oleksii.morozov.sapperthehero.boardloader.BoardLoaderImpl;

public class BoardLoaderTest {
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
	public void shouldExistFileToLoad() throws FileNotFoundException, IOException {
		boardloader.readFile("./boards/board1asda");
	}
	
	
}