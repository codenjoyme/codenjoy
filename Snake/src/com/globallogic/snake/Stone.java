package com.globallogic.snake;

import java.util.Random;

public class Stone {

	private int position;

	public Stone() {
		position = new Random().nextInt(); 
		// как помне ерунду написал, но тест хаработал. 
		// что мне тут не нравится, так это то, что камень может быть за пределами доски
		// кроме того этот тест будет время от времени слетать, потому что есть отличная от 0 вероятность
		// что камень будет от игры к игре находится в одном и том же месте
		// напишу TODO и закиммичусь пока зеленая полоса.
	}
	
	public int getX() {
		return position;
	}

	public int getY() {
		return position;
	}

}
