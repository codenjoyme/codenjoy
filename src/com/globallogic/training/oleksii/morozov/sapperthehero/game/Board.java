package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.CellImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

public interface Board {

	public List<CellImpl> getFreeCells();

	public List<CellImpl> getCells();

	public int getSize();

	public Sapper getSapper();

	public List<Mine> getMines();

	public int getMinesCount();

	public void sapperMoveTo(Direction direction);

	public boolean isSapperOnMine();

	public boolean isGameOver();

	public Cell getCellPossiblePosition(Direction direction);

	public Mine createMineOnPositionIfPossible(Cell cell);

	public int getTurn();

	public int getMinesNearSapper();

	public void useMineDetectorToGivenDirection(Direction direction);

	public boolean isEmptyDetectorButPresentMines();

	public boolean isWin();

}