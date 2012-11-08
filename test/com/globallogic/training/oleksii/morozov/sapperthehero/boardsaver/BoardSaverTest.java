package com.globallogic.training.oleksii.morozov.sapperthehero.boardsaver;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardSaverTest {

	@Test
	public void shouldBoardSaver_whenGameStart() {
		Saver boardSaver = new BoardSaver();
		assertNotNull(boardSaver);
	}

}
