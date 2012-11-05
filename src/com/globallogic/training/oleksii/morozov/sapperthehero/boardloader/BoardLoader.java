package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.FileNotFoundException;
import java.io.IOException;


public interface BoardLoader{

	public String readFile(String fileName) throws FileNotFoundException, IOException;

}
