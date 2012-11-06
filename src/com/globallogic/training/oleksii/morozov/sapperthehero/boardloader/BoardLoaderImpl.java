package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.FileMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;

public class BoardLoaderImpl implements BoardLoader {
	private String dataFromfile;

	@Override
	public void readFile(int fileNumber) {
		String fileName = "boards/board";
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			String text = null;
			reader = new BufferedReader(new FileReader(fileName + fileNumber));

			while ((text = reader.readLine()) != null) {
				result.append(text);
			}
			this.dataFromfile = result.toString();

		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException();
		} catch (IOException ioException) {
			throw new IllegalArgumentException();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioException) {
					throw new IllegalArgumentException();
				}
			}
		}
	}

	@Override
	public Board getBoard(int fileNumber) {
		readFile(fileNumber);

		return new BoardImpl(getBoardSize(), getMinesCount(), getCharge(),
				new FileMinesGenerator(getCoordinates()));
	}

	private int[] getCoordinates() {
		String[] splittedData = dataFromfile.split(" ");
		int[] result = new int[splittedData.length - 2];
		for (int index = 2; index < splittedData.length; index++) {
			result[index - 2] = Integer.parseInt(splittedData[index]);
		}
		return result;
	}

	@Override
	public int getCharge() {
		try {
			return Integer.parseInt(dataFromfile.split(" ")[1]);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

	public String getData() {
		return dataFromfile;
	}

	@Override
	public int getBoardSize() {
		try {
			return Integer.parseInt(dataFromfile.split(" ")[0]);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int getMinesCount() {
		try {
			int numberCoordinates = 2;
			int dataBoardSizeAndChargeCount = 2;
			return (dataFromfile.split(" ").length - dataBoardSizeAndChargeCount)
					/ numberCoordinates;
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

}
