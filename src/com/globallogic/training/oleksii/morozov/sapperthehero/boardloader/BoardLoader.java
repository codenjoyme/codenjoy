package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

public interface BoardLoader {

	int getCharge();

	public int getBoardSize();

	int getMinesCount();

	void readFile(int fileNumber) throws FileNotFoundException, IOException;

	Board getBoard(int fileNumber) throws IOException, FileNotFoundException;

}
