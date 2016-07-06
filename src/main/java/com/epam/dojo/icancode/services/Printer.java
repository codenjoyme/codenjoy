package com.epam.dojo.icancode.services;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.ICanCode;
import com.epam.dojo.icancode.model.Player;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.items.BaseItem;

/**
 * Created by Mikhail_Udalyi on 22.06.2016.
 */
public class Printer {

    private int size;
    private ICanCode game;

    private int viewSize;
    private int vx;
    private int vy;
    private int bound;

    private boolean needToCenter;

    public Printer(ICanCode game, int viewSize) {
        this.game = game;
        this.viewSize = Math.min(game.size(), viewSize);
        bound = this.viewSize == viewSize ? 4 : 0;
        needToCenter = bound != 0;
    }

    public String[] getBoardAsString(int numLayers, Player player) {
        StringBuilder[] builders = new StringBuilder[numLayers];
        ICell[] cells = game.getCurrentLevel().getCells();
        size = game.size();
        LengthToXY xy = new LengthToXY(size);
        Point pivot = player.getHero().getPosition();

        //If it is the first start that we will must to center position
        if (needToCenter) {
            needToCenter = false;
            moveToCenter(pivot);
        } else {
            moveTo(pivot);
        }
        adjustView(size);

        for (int i = 0; i < numLayers; ++i) {
            builders[i] = new StringBuilder(viewSize * viewSize + viewSize);
        }

        int index;
        BaseItem item;

        for (int y = vy + viewSize - 1; y >= vy; --y) {
            for (int x = vx; x < vx + viewSize; ++x) {
                index = xy.getLength(x, y);

                for (int j = 0; j < numLayers; ++j) {
                    item = cells[index].getItem(j);
                    builders[j].append(item != null ? item.state(player, item.getItemsInSameCell().toArray()).ch() : '-');

                    if (x - vx == viewSize - 1) {
                        builders[j].append('\n');
                    }
                }
            }
        }

        String[] result = new String[numLayers];

        for (int i = 0; i < numLayers; ++i) {
            result[i] = builders[i].toString();
        }

        return result;
    }

    private void moveTo(Point point) {
        int left = point.getX() - (vx + bound);
        left = left < 0 ? left : 0;

        int right = point.getX() - (vx + viewSize - bound - 1);
        right = right > 0 ? right : 0;

        int bottom = point.getY() - (vy + bound);
        bottom = bottom < 0 ? bottom : 0;

        int up = point.getY() - (vy + viewSize - bound - 1);
        up = up > 0 ? up : 0;

        vx += left + right;
        vy += up + bottom;
    }

    private void moveToCenter(Point point)
    {
        vx = point.getX() - Math.round(viewSize / 2);
        vy = point.getY() - Math.round(viewSize / 2);
    }

    private void adjustView(int size)
    {
        vx = vx < 0 ? 0 : vx;
        vx = vx + viewSize > size ? size - viewSize : vx;

        vy = vy < 0 ? 0 : vy;
        vy = vy + viewSize > size ? size - viewSize : vy;
    }

    public String print(Player player) {
        String[] layers = getBoardAsString(2, player);

        return String.format("[\"%s\",\"%s\"]",
                layers[0],
                layers[1]);
    }
}
