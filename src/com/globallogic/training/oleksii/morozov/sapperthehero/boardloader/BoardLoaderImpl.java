package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;

public class BoardLoaderImpl implements BoardLoader{
	

	@Override
	public String readFile(String fileName) throws IOException, FileNotFoundException{
		File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
		try {
			String text = null;
			reader = new BufferedReader(new FileReader(file));
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(
						System.getProperty("line.separator"));
			}
			return contents.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	@Override
	public Board getBoard() {
		return new BoardImpl(5, 5, 10, new MinesGenerator() {
			@Override
			public List<Mine> get(int count, Board board) {
				return Arrays.asList(new Mine(1,1));
			}
		});
	}

	@Override
	public int getCharge(String fileName) throws FileNotFoundException, IOException {
		String fileStream= readFile(fileName);
		fileStream.indexOf("*");
		return 0;
	}
	
}
