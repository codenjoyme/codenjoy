package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

public interface BoardLoader {



	int getCharge();

	public int getBoardSize();

	int getMinesCount();

	void readFile(int fileNumber);

	Board getBoard(int fileNumber);

}
