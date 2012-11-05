package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.CellImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

public interface Board {

	public abstract List<CellImpl> getFreeCells();

	public abstract List<CellImpl> getCells();

	public abstract int getSize();

	public abstract Sapper getSapper();

	public abstract List<Mine> getMines();

	public abstract int getMinesCount();

	public abstract void sapperMoveTo(Direction direction);

	public abstract boolean isSapperOnMine();

	public abstract boolean isGameOver();

	public abstract Cell getCellPossiblePosition(Direction direction);

	public abstract Mine createMineOnPositionIfPossible(Cell cell);

	public abstract int getTurn();

	public abstract int getMinesNearSapper();

	public abstract void useMineDetectorToGivenDirection(Direction direction);

	public abstract boolean isEmptyDetectorButPresentMines();

	public abstract boolean isWin();

}