package com.leokom;

public class Position {
	private int row;
	private int column;

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return row + ":" + column;
	}

	// no need square rooting
	public int distanceFrom(Position position) {
		return (row - position.row) * (row - position.row)
				+ (column - position.column) * (column - position.column);
	}

	public Position left() {
		if (this.getColumn() == 0) {
			return null;
		}

		return new Position(this.getRow(), this.getColumn() - 1);
	}

	public Position right() {
		if (this.getColumn() == Matrix.COLUMNS - 1) {
			return null;
		}

		return new Position(this.getRow(), this.getColumn() + 1);
	}

	public Position top() {
		if (this.getRow() == 0) {
			return null;
		}

		return new Position(this.getRow() - 1, this.getColumn());
	}

	public Position bottom() {
		if (this.getRow() == Matrix.ROWS - 1) { // TODO: cycle dependency
			return null;
		}

		return new Position(this.getRow() + 1, this.getColumn());
	}

	public Position to(Direction direction) {
		switch (direction) {
		case LEFT:
			return left();
		case RIGHT:
			return right();
		case DOWN:
			return bottom();
		case UP:
			return top();
		default:
			throw new IllegalArgumentException("Direction not supported: "
					+ direction);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof Position ) ) {
			return false;
		}
		Position rhs = ( Position ) obj;
		return column == rhs.column && row == rhs.row;
	}
}
