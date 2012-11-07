package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.CellImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

 public interface Board {

	 List<CellImpl> getFreeCells();

	 List<CellImpl> getCells();

	 int getSize();

	 Sapper getSapper();

	 List<Mine> getMines();

	 int getMinesCount();

	 void sapperMoveTo(Direction direction);

	 boolean isSapperOnMine();

	 boolean isGameOver();

	 Cell getCellPossiblePosition(Direction direction);

	 Mine createMineOnPositionIfPossible(Cell cell);

	 int getTurn();

	 int getMinesNearSapper();

	 void useMineDetectorToGivenDirection(Direction direction);

	 boolean isEmptyDetectorButPresentMines();

	 boolean isWin();

}