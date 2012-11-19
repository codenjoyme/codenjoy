package net.tetris.dom;

import junit.framework.Assert;
import net.tetris.services.*;
import net.tetris.services.levels.MockLevels;
import org.junit.Test;

import static junit.framework.Assert.assertSame;
import static org.fest.reflect.core.Reflection.field;
import static org.junit.Assert.assertEquals;

public class PlayerBuilderTest {

    private Player player;

    @Test
    public void shouldGetLevelsDataWhenCallLevelsReader() {
        Levels levels = new MockLevelsBuilder().getLevels();

        player = new Player("name", "url", new PlayerScores(100), levels, new Information() {
            @Override
            public String getMessage() {
                return "info";
            }
        });

        Player.PlayerReader reader = player.new PlayerReader();
        assertEquals(100, reader.getScores());
        assertEquals("url", reader.getCallbackUrl());
        assertEquals("info", reader.getInformation());
        assertEquals("name", reader.getName());

        assertEquals("MockLevels", reader.getLevels().getLevelsName());
        assertEquals(1, reader.getLevels().getCurrentLevel());
        assertEquals(4,reader.getLevels().getTotalRemovedLines());
    }

    class MockLevelsBuilder extends Levels.LevelsBuilder {
        public Levels getLevels() {
            MockLevels levels = new MockLevels(new PlayerFigures());
            levels.linesRemoved(MockLevels.LINES_REMOVED_FOR_NEXT_LEVEL);
            assertEquals(1, levels.getCurrentLevelNumber());
            return levels;
        }
    }

    @Test
    public void shouldSetPlayerPropertiesWhenBuildPlayer() {
        Player.PlayerBuilder builder = new Player.PlayerBuilder();
        builder.setCallbackUrl("url");
        builder.setInformation("info");
        builder.setName("name");
        builder.setScores(100);
        builder.setLevels(new MockLevelsBuilder());

        player = builder.getPlayer();

        assertEquals("info, Level 2", player.getMessage());
        assertEquals("url", player.getCallbackUrl());
        assertEquals("name", player.getName());
        assertEquals(100, player.getScore());
        assertEquals(4, player.getTotalRemovedLines());
        assertEquals(1, player.getCurrentLevelNumber());

        assertEquals(Figure.Type.O, getLevels().getCurrentLevel().getFigureQueue().next().getType());
    }

    private Levels getLevels() {
        return field("levels").ofType(Levels.class).in(player).get();
    }

    @Test
     public void shouldInformationCollectorWrappedPlayerScoresWhenBuildPlayer() {
        shouldSetPlayerPropertiesWhenBuildPlayer();
        InformationCollector info = getInformationCollector();

        assertEquals(100, player.getScore());
        info.linesRemoved(4);
        assertEquals(200, player.getScore());
    }

    private InformationCollector getInformationCollector() {
        return (InformationCollector)field("info").ofType(Information.class).in(player).get();
    }

    @Test
    public void shouldInformationCollectorListenOnChangeLevel() {
        shouldSetPlayerPropertiesWhenBuildPlayer();

        assertEquals(1, player.getCurrentLevelNumber());
        getLevels().linesRemoved(4);
        assertEquals(2, player.getCurrentLevelNumber());
        assertEquals("Level 3", player.getMessage());
    }

    @Test
    public void shouldIgnoreNullMessageOnChangeLevel() {
        Player.PlayerBuilder builder = new Player.PlayerBuilder();
        builder.setCallbackUrl("url");
        builder.setName("name");
        builder.setScores(100);
        builder.setLevels(new MockLevelsBuilder());

        builder.setInformation(null);

        player = builder.getPlayer();

        assertEquals("Level 2", player.getMessage());
    }

    @Test
    public void shouldPlayerScoresWillUpdateOnLevelChange() {
        shouldSetPlayerPropertiesWhenBuildPlayer();

        assertEquals(1, player.getCurrentLevelNumber());
        getInformationCollector().linesRemoved(4);
        assertEquals(100 + 100, player.getScore());

        getLevels().linesRemoved(4);
        assertEquals(2, player.getCurrentLevelNumber());

        getInformationCollector().linesRemoved(4);
        assertEquals(200 + 2*100, player.getScore());
    }
}
