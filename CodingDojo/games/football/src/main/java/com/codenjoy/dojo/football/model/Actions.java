package com.codenjoy.dojo.football.model;

public enum Actions {

	HIT_UP(1),
	HIT_RIGHT(2),
	HIT_DOWN(3),
	HIT_LEFT(4),
	STOP_BALL(5),
	/*UP(6),
	RIGHT(7),
	DOWN(8),
	LEFT(9),
	SET_MAIN_HERO(10)*/;
		
	private int value;

	Actions(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Actions valueOf(int[] p) {
		// TODO Auto-generated method stub
		return null;
	}
}
