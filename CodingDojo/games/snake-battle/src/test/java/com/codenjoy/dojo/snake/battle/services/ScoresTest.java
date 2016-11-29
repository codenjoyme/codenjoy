package com.codenjoy.dojo.snake.battle.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author K.ilya
 */
public class ScoresTest {

	Scores scores;

	@Before
	public void prepare() {
		prepare(0);
	}

	public void prepare(int startScore) {
		scores = new Scores(startScore, new SettingsImpl());
	}

	@Test
	public void eatApple() {
		scores.event(Events.APPLE);
		assertTrue("При поедании яблока, счёт не увеличился! ("+scores.getScore()+")", 1 == scores.getScore());
		scores.event(Events.APPLE);
		assertTrue("При поедании яблока, счёт не увеличился! ("+scores.getScore()+")", 2 == scores.getScore());
	}

	@Test
	public void eatStone() {
		prepare(100);
		scores.event(Events.STONE);
		assertTrue("При поедании камня, счёт не уменьшился! ("+scores.getScore()+")", 99 == scores.getScore());
		scores.event(Events.STONE);
		assertTrue("При поедании камня, счёт не уменьшился! ("+scores.getScore()+")", 98 == scores.getScore());
	}

	@Test
	public void winGame() {
		scores.event(Events.WIN);
		assertTrue("При выигрыше, счёт не увеличился! ("+scores.getScore()+")", 30 == scores.getScore());
		scores.event(Events.WIN);
		assertTrue("При выигрыше, счёт не увеличился! ("+scores.getScore()+")", 60 == scores.getScore());
	}

	@Test
	public void die() {
		prepare(100);
		scores.event(Events.DIE);
		assertTrue("При гибели, счёт не уменьшился! ("+scores.getScore()+")", 90 == scores.getScore());
		scores.event(Events.DIE);
		assertTrue("При гибели, счёт не уменьшился! ("+scores.getScore()+")", 80 == scores.getScore());
	}
}
