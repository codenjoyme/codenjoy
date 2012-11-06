package com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator;

import java.util.ArrayList;
import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;

public class FileMinesGenerator implements MinesGenerator {

	private int[] mineCoordinates;

	public FileMinesGenerator(int[] mineCoordinates) {
		this.mineCoordinates = mineCoordinates;
	}

	@Override
	public List<Mine> get(int count, Board board) {
		List<Mine> result = new ArrayList<Mine>();
		for (int index = 0; index < mineCoordinates.length - 1; index += 2) {
			result.add(new Mine(index, index + 1));
		}
		return result;
	}

}
