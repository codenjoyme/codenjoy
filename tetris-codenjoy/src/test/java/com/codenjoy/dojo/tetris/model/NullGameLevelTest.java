package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.NullGameLevel;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class NullGameLevelTest {

    @Test
    public void shouldNoApplyNextLevelWhenAccepted() {
        assertFalse("Levels terminator should not apply any events",
                new NullGameLevel().accept(mock(GlassEvent.class)));
    }

}
