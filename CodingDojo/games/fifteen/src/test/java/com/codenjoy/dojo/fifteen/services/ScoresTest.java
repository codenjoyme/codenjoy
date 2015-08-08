package com.codenjoy.dojo.fifteen.services;

import com.codenjoy.dojo.fifteen.model.Bonus;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScoresTest {
    private static final int MOVE_COUNT = 2;
    private static final int NUMBER = 5;

    private PlayerScores scores;

    private Settings settings;
    private Integer winScore;
    private Integer bonusScore;

    public void win() {
        scores.event(Events.WIN);
    }

    public void bonus() {
        scores.event(new Bonus(MOVE_COUNT, NUMBER));
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
        bonusScore = settings.getParameter("Bonus score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(250, settings);

        bonus(); // + 100
        bonus(); // + 100

        Assert.assertEquals(250 + 2 * bonusScore * NUMBER / MOVE_COUNT, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenWin() {
        scores = new Scores(250, settings);

        win(); // + 1000

        Assert.assertEquals(250 + winScore, scores.getScore());
    }
}
