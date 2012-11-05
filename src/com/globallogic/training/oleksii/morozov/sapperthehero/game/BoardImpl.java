package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.CellImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardImpl implements Board{

    private List<CellImpl> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;

    public BoardImpl(int size, int minesCount, int detectorCharge, MinesGenerator minesGenerator) {
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException();
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException();
        }
        this.minesGenerator = minesGenerator;
        this.size = size;
        this.cells = initializeBoardCells(size);
        this.sapper = initializeSapper();
        this.sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = this.minesGenerator.get(minesCount, this);
    }


    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }
    
    private List<CellImpl> initializeBoardCells(int size) {
        List<CellImpl> result = new ArrayList<CellImpl>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new CellImpl(x, y));
            }
        }
        return result;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getFreeCells()
	 */
    @Override
	public List<CellImpl> getFreeCells() {
		List<CellImpl> result = new LinkedList<CellImpl>();
		for (CellImpl cell : getCells()) {
			boolean isSapper = cell.equals(getSapper());
			boolean isMine = isMine(cell);
			if (!isSapper && !isMine) {
				result.add(cell);
			}
		}
		return result;
	}

	private boolean isMine(Cell cell) {
		boolean isMine = false;
		if (getMines() != null) {
			for (Mine mine : getMines()) {
				isMine |= cell.equals(mine);
			}
		}
		return isMine;
	}

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getCells()
	 */
    @Override
	public List<CellImpl> getCells() {
        return cells;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getSize()
	 */
    @Override
	public int getSize() {
        return size;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getSapper()
	 */
    @Override
	public Sapper getSapper() {
        return sapper;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getMines()
	 */
    @Override
	public List<Mine> getMines() {
        return mines;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getMinesCount()
	 */
    @Override
	public int getMinesCount() {
        return getMines().size();
    }


    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#sapperMoveTo(com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction)
	 */
    @Override
	public void sapperMoveTo(Direction direction) {
        if (isSapperCanMoveToDirection(direction)) {
            moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die(true);
            }
            nextTurn();
        }
    }

    private void moveSapperAndFillFreeCell(Direction direction) {
        sapper.displaceMeByDelta(direction.getDeltaPosition());
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        return cells.contains(result);
    }

    private void nextTurn() {
        turnCount++;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#isSapperOnMine()
	 */
    @Override
	public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#isGameOver()
	 */
    @Override
	public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines();
    }


    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getCellPossiblePosition(com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction)
	 */
    @Override
	public Cell getCellPossiblePosition(Direction direction) {
        Cell result = sapper.clone();
        result.changeMyCoordinate(direction.getDeltaPosition());
        return result;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#createMineOnPositionIfPossible(com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell)
	 */
    @Override
	public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getTurn()
	 */
    @Override
	public int getTurn() {
        return turnCount;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#getMinesNearSapper()
	 */
    @Override
	public int getMinesNearSapper() {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getCellPossiblePosition(direction);
            if (cells.contains(sapperPossiblePosition) && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#useMineDetectorToGivenDirection(com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction)
	 */
    @Override
	public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                destroyMine(result);
            }
        }
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#isEmptyDetectorButPresentMines()
	 */
    @Override
	public boolean isEmptyDetectorButPresentMines() {
        return mines.size() != 0 && sapper.getMineDetectorCharge() == 0;
    }

    /* (non-Javadoc)
	 * @see com.globallogic.training.oleksii.morozov.sapperthehero.game.Board#isWin()
	 */
    @Override
	public boolean isWin() {
        return mines.size() == 0 && !sapper.isDead();
    }

    private void destroyMine(Cell possibleMine) {
        getMines().remove(possibleMine);
    }

}
