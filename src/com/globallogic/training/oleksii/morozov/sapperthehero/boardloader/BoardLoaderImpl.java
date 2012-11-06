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
	public void readFile(int fileNumber) throws IOException,
			FileNotFoundException {
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

		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	@Override
	public Board getBoard(int fileNumber) throws IOException,
			FileNotFoundException {
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
		return Integer.parseInt(dataFromfile.split(" ")[1]);
	}

	@Override
	public int getBoardSize() {
		return Integer.parseInt(dataFromfile.split(" ")[0]);
	}

	@Override
	public int getMinesCount() {
		int numberCoordinates = 2;
		int dataBoardSizeAndChargeCount = 2;
		return (dataFromfile.split(" ").length - dataBoardSizeAndChargeCount)
				/ numberCoordinates;
	}

}
