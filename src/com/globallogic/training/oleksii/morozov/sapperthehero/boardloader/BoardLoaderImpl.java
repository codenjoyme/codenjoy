package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

}
