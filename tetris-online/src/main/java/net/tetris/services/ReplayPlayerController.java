package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:46 PM
 */
@Component("replayPlayerController")
@Scope("prototype")
public class ReplayPlayerController implements PlayerController {
    @Override
    public void requestControl(Player player, Figure.Type type, int x, int y, Joystick joystick, List<Plot> plots) throws IOException {
    }
}
