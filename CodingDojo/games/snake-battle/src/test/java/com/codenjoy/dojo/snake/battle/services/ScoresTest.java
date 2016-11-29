package com.codenjoy.dojo.snake.battle.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * @author K.ilya
 */
@RunWith(Parameterized.class)
public class ScoresTest {

	Scores scores;
	Events event;
	int changeValue;

	public ScoresTest(int startScore, Events event, int changeValue) {
		scores = new Scores(startScore, new SettingsImpl());
		this.event = event;
		this.changeValue = changeValue;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Object[][] params = new Object[][]{
				{0, Events.APPLE, +1},
				{0, Events.STONE, 0}, // счёт всегда >=0
				{0, Events.WIN, +30},
				{0, Events.DIE, 0}, // счёт всегда >=0
				{100, Events.APPLE, +1},
				{100, Events.STONE, -1},
				{100, Events.WIN, +30},
				{100, Events.DIE, -10},
		};
		return Arrays.asList(params);
	}

	@Test
	public void eventTest() {
		for (int i = 0; i < 2; i++) {
			int before = scores.getScore();
			scores.event(event);
			int after = scores.getScore();
			assertTrue("После события '" + event + "', счёт не корректен! (до " + before + ", после " + after + ")",
					before + changeValue == after);
		}
	}
}
