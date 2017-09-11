package com.epam.dojo.expansion.model.lobby;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Field;
import com.epam.dojo.expansion.model.ForcesMoves;
import com.epam.dojo.expansion.model.levels.Cell;
import com.epam.dojo.expansion.model.levels.CellImpl;
import com.epam.dojo.expansion.model.levels.Item;
import com.epam.dojo.expansion.model.levels.items.BaseItem;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import com.epam.dojo.expansion.model.levels.items.Start;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class Lobby implements Field {
    @Override
    public void increase(Hero hero, List<ForcesMoves> increase) {
        // do nothing
    }

    @Override
    public void move(Hero hero, List<ForcesMoves> movements) {
        // do nothing
    }

    @Override
    public Start getBaseOf(Hero hero) {
        return getStart(hero.getPosition());
    }

    @Nullable
    @Override
    public Start getFreeBase() {
        return getStart(pt(0, 0));
    }

    @NotNull
    private Start getStart(Point pt) {
        Start start = new Start(Elements.BASE1);
        start.setCell(new CellImpl(pt));
        return start;
    }

    @Override
    public HeroForces startMoveForces(Hero item, int x, int y, int count) {
        return HeroForces.EMPTY;
    }

    @Override
    public void removeForces(Hero hero, int x, int y) {
        // do nothing
    }

    @Override
    public void reset() {
        // do nothing
    }

    @Override
    public void removeFromCell(Hero hero) {
        // do nothing
    }

    @Override
    public int totalRegions() {
        return 1;
    }

    @Override
    public int regionsCount(Hero hero) {
        return 0;
    }
}
