package com.epam.dojo.icancode.services;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.ICanCode;
import com.epam.dojo.icancode.model.Player;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IItem;

/**
 * Created by Mikhail_Udalyi on 22.06.2016.
 */
public class Printer {

    private static final int BOUND_DEFAULT = 4;

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

        if (this.viewSize == viewSize) {
            bound = BOUND_DEFAULT;
        }

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
        IItem item;

        for (int y = vy + viewSize - 1; y >= vy; --y) {
            for (int x = vx; x < vx + viewSize; ++x) {
                index = xy.getLength(x, y);

                for (int j = 0; j < numLayers; ++j) {
                    item = cells[index].getItem(j);
                    builders[j].append(makeState(item, player, x));
                }
            }
        }

        String[] result = new String[numLayers];

        for (int i = 0; i < numLayers; ++i) {
            result[i] = builders[i].toString();
        }

        return result;
    }

    private String makeState(IItem item, Player player, int x) {
        char result;

        if (item != null) {
            result = item.state(player, item.getItemsInSameCell().toArray()).ch();
        } else {
            result = '-';
        }

        if (x - vx == viewSize - 1) {
            return new String(new char[]{result, '\n'});
        }

        return String.valueOf(result);
    }

    private void moveTo(Point point) {
        int left = point.getX() - (vx + bound);
        left = fixToNegative(left);

        int right = point.getX() - (vx + viewSize - bound - 1);
        right = fixToPositive(right);

        int bottom = point.getY() - (vy + bound);
        bottom = fixToNegative(bottom);

        int up = point.getY() - (vy + viewSize - bound - 1);
        up = fixToPositive(up);

        vx += left + right;
        vy += up + bottom;
    }

    private int fixToPositive(int value) {
        if (value < 0) {
            return 0;
        }

        return value;
    }

    private int fixToNegative(int value) {
        if (value > 0) {
            return 0;
        }

        return value;
    }

    private void moveToCenter(Point point)
    {
        vx = (int) (point.getX() - Math.round((double) viewSize / 2));
        vy = (int) (point.getY() - Math.round((double) viewSize / 2));
    }

    private void adjustView(int size)
    {
        vx = fixToPositive(vx);
        if (vx + viewSize > size) {
            vx = size - viewSize;
        }

        vy = fixToPositive(vy);
        if (vy + viewSize > size) {
            vy = size - viewSize;
        }
    }

    public String print(Player player) {
        String[] layers = getBoardAsString(2, player);

        return String.format("[\"%s\",\"%s\"]",
                layers[0],
                layers[1]);
    }
}
