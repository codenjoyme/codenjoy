package net.tetris.services;

import com.codenjoy.dojo.transport.GameState;
import net.tetris.dom.Figure;
import net.tetris.dom.TetrisGame;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
* User: serhiy.zelenin
* Date: 4/8/13
* Time: 11:06 PM
*/
class TetrisGameState implements GameState {

    private final List<Plot> plots;
    private final Figure.Type type;
    private final int x;
    private final int y;
    private final List<Figure.Type> futureFigures;
    private boolean toEncodeUrl;

    public TetrisGameState(List<Plot> plots, Figure.Type type, int x, int y, List<Figure.Type> futureFigures, boolean toEncodeUrl) {
        this.plots = plots;
        this.type = type;
        this.x = x;
        this.y = y;
        this.futureFigures = futureFigures;
        this.toEncodeUrl = toEncodeUrl;
    }

    @Override
    public String asString() {
        StringBuilder content = new StringBuilder();
        StringBuilder sb = exportGlassState(plots);

        try {
            String glassContent;
            if (toEncodeUrl) {
                glassContent = URLEncoder.encode(sb.toString(), "UTF-8");
            } else {
                glassContent = sb.toString();
            }
            content.append("figure=").append(type).append("&x=").append(x).append("&y=").append(y).append("&glass=").append(glassContent);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if (futureFigures != null && !futureFigures.isEmpty()) {
            content.append("&next=");
            for (Figure.Type futureFigure : futureFigures) {
                content.append(futureFigure.getName());
            }
        }
        return content.toString();
    }

    private StringBuilder exportGlassState(List<Plot> plots) {
        char[][] glassState = new char[TetrisGame.GLASS_HEIGHT][TetrisGame.GLASS_WIDTH];
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            Arrays.fill(glassState[i], ' ');
        }

        for (Plot plot : plots) {
            glassState[plot.getY()][plot.getX()] = '*';
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            sb.append(glassState[i]);
        }
        return sb;
    }

}
