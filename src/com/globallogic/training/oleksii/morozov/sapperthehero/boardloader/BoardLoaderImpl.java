package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.FileMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;

public class BoardLoaderImpl implements BoardLoader {
	private static final String WHITESPACE_SPLITTER = "\\s";
	private static final String FILE_PATH = "boards/board";
	int boarSize;
	int charge;
	List<Mine> mines;

	@Override
	public void readFromFile(int fileNumber) throws IOException,
			FileNotFoundException {
	
		BufferedReader reader = null;
		mines = new ArrayList<Mine>();
		try {
			String text = null;
			reader = new BufferedReader(new FileReader(FILE_PATH + fileNumber));
			boolean isChargeAndBoardSizeData = true;
			while ((text = reader.readLine()) != null) {
				int firstCollumn = Integer.parseInt((text
						.split(WHITESPACE_SPLITTER))[0]);
				int secondCollumn = Integer.parseInt((text
						.split(WHITESPACE_SPLITTER))[1]);
				if (isChargeAndBoardSizeData) {
					boarSize = firstCollumn;
					charge = secondCollumn;
					isChargeAndBoardSizeData = false;
				} else {
					mines.add(new Mine(firstCollumn, secondCollumn));
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	@Override
	public Board getBoard(int fileNumber) throws IOException,
			FileNotFoundException {
		readFromFile(fileNumber);

		return new BoardImpl(getBoardSize(), getMinesCount(), getCharge(),
				new FileMinesGenerator(mines));
	}

	@Override
	public int getCharge() {
		return charge;
	}

	@Override
	public int getBoardSize() {
		return boarSize;
	}

	@Override
	public int getMinesCount() {
		return mines.size();
	}

	@Override
	public void saveBoard(Board board, int fileNumber) {
		String fileName = FILE_PATH + fileNumber;
		
		
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(fileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.writeObject(board);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		

	
	
}
