package com.globallogic.training.oleksii.morozov.sapperthehero.game.objects;

public interface Cell {

	public abstract int getX();

	public abstract int getY();

	public abstract void changeMyCoordinate(Cell cell);

}