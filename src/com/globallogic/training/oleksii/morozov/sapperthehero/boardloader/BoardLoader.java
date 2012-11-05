package com.globallogic.training.oleksii.morozov.sapperthehero.boardloader;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;


public interface BoardLoader{

	public String readFile(String fileName) throws FileNotFoundException, IOException;

	public Board getBoard();

}
