package net.tetris.dom;

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
