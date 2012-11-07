package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

public interface BoardLoader {

	int getCharge();

	int getBoardSize();

	int getMinesCount();

	void readFromFile(int fileNumber);

	Board getBoard(int fileNumber);

	void saveBoard(Board board, int fileNumber);

}
