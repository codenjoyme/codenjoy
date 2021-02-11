package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.expansion.model.levels.Cell;
import com.codenjoy.dojo.expansion.model.levels.Item;
import com.codenjoy.dojo.expansion.model.levels.Level;
import com.codenjoy.dojo.expansion.model.levels.items.*;
import com.codenjoy.dojo.expansion.model.replay.GameLogger;
import com.codenjoy.dojo.expansion.services.Events;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;

public class Expansion implements Tickable, IField {

    public static final Events WIN_MULTIPLE = Events.WIN(data.winScore());
    public static final Events DRAW_MULTIPLE = Events.WIN(data.drawScore());
    public static final Events WIN_SINGLE = Events.WIN(0);
    public static final Events LOOSE = Events.LOOSE();

    private static Logger logger = DLoggerFactory.getLogger(Expansion.class);

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;
    private GameLogger gameLogger;

    private Level level;
    private final Ticker ticker;
    private final Dice dice;

    private boolean isMultiplayer;
    private boolean nothingChanged;

    private List<Player> players;
    private List<Player> losers;
    private int roundTicks;

    public Expansion(Level level, Ticker ticker, Dice dice, GameLogger gameLogger, boolean multiple) {
        this.level = level;
        this.ticker = ticker;
        this.dice = dice;
        level.setField(this);
        isMultiplayer = multiple;
        players = new LinkedList();
        this.gameLogger = gameLogger;
        cleanAfterGame();
    }

    private void cleanAfterGame() {
        roundTicks = 0;
        nothingChanged = true;
        losers = new LinkedList();
        if (isMultiplayer) {
            gameLogger.start(this);
        }
    }

    @Override
    public void tick() {
        if (logger.isDebugEnabled()) {
            logger.debug("Expansion {} started tick", lg.id());
        }

        ticker.tick();

        if (isWaitingOthers()) return;

        roundTicks++;

        if (logger.isDebugEnabled()) {
            logger.debug("Expansion processing board calculations. " +
                            "State before processing {}",
                    toString());
        }

        if (isMultiplayer) {
            gameLogger.logState();
        }

        if (isMultiplayer) {
            Player winner = null;
            for (Player player : players) {
                Hero hero = player.getHero();

                Events status = checkStatus(player, hero);
                if (status != null) {
                    player.event(status);
                }

                if (Arrays.asList(WIN_MULTIPLE, DRAW_MULTIPLE).contains(status)) {
                    winner = player;
                }
            }

            if (winner != null) {
                resetAllPlayers();
            }
        }

        if (data.roundLimitedInTime()) {
            if (roundTicks >= data.roundTicks()) {
                for (Player player : players) {
                    if (losers.contains(player)) continue;
                    player.event(DRAW_MULTIPLE);
                }
                resetAllPlayers();

                if (logger.isDebugEnabled()) {
                    logger.debug("Expansion round is out. All players will be removed! {}",
                            toString());
                }
            }
        }

        // there player level be changed
        for (Player player : players.toArray(new Player[0])) {
            player.getHero().tick();
        }

        if (!players.isEmpty()) {
            attack();

            for (Tickable item : level.getItems(Tickable.class)) {
                if (item instanceof Hero) {
                    continue;
                }

                item.tick();
            }

            for (Player player : players) {
                Hero hero = player.getHero();

                if (hero.isWin()) {
                    player.event(WIN_SINGLE);
                    player.goToNextLevel();
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Expansion finished tick. " +
                    "State after processing {}", toString());
        }
    }

    private void resetAllPlayers() {
        // fist time remove all players
        List<Player> reset = removeAllPlayers();
        // then add they to this board
        for (Player player : reset) {
            newGame(player);
        }
    }

    @NotNull
    private List<Player> removeAllPlayers() {
        List<Player> result = new LinkedList<>(players);
        for (Player player : result) {
            remove(player);
        }
        return result;
    }

    private void attack() {
        if (logger.isDebugEnabled()) {
            countChecker.before();
        }

        for (Cell cell : level.getCellsWith(HeroForces.class)) {
            List<HeroForces> forces = cell.getItems(HeroForces.class);
            if (forces.size() <= 1) continue;

            nothingChanged &= !data.attack().calculate(forces);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("During call attack() method for game {} found this " +
                            "forces count delta {} (it should be <= 0!)",
                    lg.id(),
                    countChecker.after());
        }
    }

    private LawOfEnergyConservationChecker countChecker = new LawOfEnergyConservationChecker();

    public boolean isNew() {
        return !isMultiplayer || (isMultiplayer && players.isEmpty());
    }

    class LawOfEnergyConservationChecker {
        private int count;

        public int count() {
            return Arrays.asList(level.getCells()).stream()
                    .mapToInt(cell -> {
                        List<HeroForces> items = cell.getItems(HeroForces.class);
                        if (items.isEmpty()) return 0;
                        return items.stream().mapToInt((heroForces) -> heroForces.getCount()).sum();
                    }).sum();
        }

        public void before(){
            this.count = count();
        }

        public int after(){
            return count() - this.count;
        }
    }

    @Override
    public void increase(Hero hero, List<ForcesMoves> increase) {
        if (logger.isDebugEnabled()) {
            countChecker.before();
        }
        nothingChanged = false;

        int total = hero.getForcesPerTick();
        for (Forces forces : increase) {
            Point to = forces.getRegion();

            if (forces.getCount() < 0) continue;
            if (isBarrier(to.getX(), to.getY())) continue;

            int count = Math.min(total, forces.getCount());
            int actual = countForces(hero, to.getX(), to.getY());
            if (actual > 0) {
                total -= count;
                startMoveForces(hero, to.getX(), to.getY(), count).move();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("During call increase() method for hero {} found " +
                            "this forces count delta {} (hero can only {}!)",
                    hero.lg.id(),
                    countChecker.after(),
                    hero.getForcesPerTick());
        }
    }

    @Override
    public void move(Hero hero, List<ForcesMoves> movements) {
        if (logger.isDebugEnabled()) {
            countChecker.before();
        }
        nothingChanged = false;

        List<HeroForces> moved = new LinkedList<>();
        for (ForcesMoves forces : movements) {
            Point from = forces.getRegion();
            Point to = forces.getDestination(from);

            if (from.equals(to)) continue;
            if (forces.getCount() < 0) continue;
            if (isBarrier(to.getX(), to.getY())) continue;

            int count = leaveForces(hero, from.getX(), from.getY(), forces.getCount());
            moved.add(startMoveForces(hero, to.getX(), to.getY(), count));
        }

        for (HeroForces force : moved) {
            force.move();
        }

        if (logger.isDebugEnabled()) {
            if (countChecker.after() != 0) {
                System.out.println();
            }
            logger.debug("During call move() method for hero {} found this " +
                            "forces count delta {} (it should be 0!)",
                    hero.lg.id(),
                    countChecker.after());
        }
    }

    private Events checkStatus(Player player, Hero hero) {
        if (losers.contains(player)) return null;
        if (players.size() == 1) {
            List<Cell> freeCells = level.getCellsWith(
                    cell -> cell.getItems(HeroForces.class).isEmpty() &&
                            cell.isPassable() && cell.getItem(Hole.class) == null
            );
            if (freeCells.isEmpty()) {
                return DRAW_MULTIPLE;
            }
            return null;
        }

        List<HeroForces> allForces = level.getItems(HeroForces.class);
        boolean alone = true;
        boolean exists = false;
        for (HeroForces item : allForces) {
            alone &= item.itsMe(hero);
            exists |= item.itsMe(hero);
        }
        if (alone) {
            return WIN_MULTIPLE;
        }
        if (!exists) {
            losers.add(player);
            player.hero.die();
            return LOOSE;
        }
        return null;
    }

    private boolean isWaitingOthers() {
        return isMultiplayer && data.waitingOthers() && gameNotStarted() && players.size() != 4;
    }

    private boolean gameNotStarted() {
        return roundTicks == 0;
    }

    @Override
    public int size() {
        return level.getSize();
    }

    private boolean isBarrier(int x, int y) {
        return level.isBarrier(x, y);
    }

    @Override
    public Start getBaseOf(Hero hero) {
        List<Start> bases = level.getItems(Start.class);

        for (Start base : bases) {
            if (base.isOwnedBy(hero)) {
                return base;
            }
        }
        return hero.occupyFreeBase();
    }

    @Override
    @Nullable
    public Start getFreeBase() {
        List<Start> bases = level.getItems(Start.class);

        Collections.sort(bases, (o1, o2) -> Integer.compare(o1.index(), o2.index()));

        Start free = null;
        for (Start place : bases) {
            if (place.isFree()) {
                free = place;
                break;
            }
        }
        return free;
    }

    @Override
    public HeroForces startMoveForces(Hero hero, int x, int y, int count) {
        if (count == 0) return HeroForces.EMPTY;

        Cell cell = level.getCell(x, y);
        HeroForces force = getHeroForces(hero, cell);

        if (force == null) {
            HeroForces income = new HeroForces(hero);
            cell.captureBy(income);
            income.startMove(count);
            return income;
        } else {
            force.startMove(count);
            return force;
        }
    }

    private HeroForces getHeroForces(Hero hero, Cell cell) {
        List<HeroForces> forces = cell.getItems(HeroForces.class);
        for (HeroForces force : forces) {
            if (force.itsMe(hero)) {
                return force;
            }
        }
        return null;
    }

    @Override
    public void removeForces(Hero hero, int x, int y) {
        Cell cell = level.getCell(x, y);
        HeroForces force = cell.getItem(HeroForces.class);
        if (force != null && force.itsMe(hero)) {
            force.removeFromCell();
        }
    }

    private int leaveForces(Hero hero, int x, int y, int count) {
        Cell cell = level.getCell(x, y);

        HeroForces force = getHeroForces(hero, cell);
        if (force == null) {
            return 0;
        }

        return force.leave(count, data.leaveForceCount());
    }

    private int countForces(Hero hero, int x, int y) {
        Cell cell = level.getCell(x, y);

        HeroForces force = getHeroForces(hero, cell);
        if (force == null) {
            return 0;
        }

        return force.getCount();
    }

    @Override
    public void reset() {
        if (isMultiplayer && players.size() > 1) {
            return;
        }

        for (Gold gold : level.getItems(Gold.class)) {
            gold.reset();
        }
    }

    @Override
    public int totalRegions(){
        return level.getCellsWith(cell -> cell.isPassable()
                && cell.getItem(Hole.class) == null).size();
    }

    @Override
    public int regionsCount(Hero hero) {
        return level.getCellsWith(cell -> cell.busy(hero)).size();
    }

    @Override
    public void newGame(Player player) {
        if (losers.contains(player)) {
            return;
        }

        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
        if (isMultiplayer) {
            gameLogger.register(player);
        }
    }

    @Override
    public void remove(Player player) {
        losers.remove(player); // TODO test me
        players.remove(player);
        player.destroyHero();
        if (players.isEmpty()) {
            cleanAfterGame();
        }
    }

    @Override
    public void removeFromCell(Hero hero) {
        for (HeroForces forces : level.getItems(HeroForces.class)) {
            if (forces.itsMe(hero)) {
                forces.removeFromCell();
            }
        }
        for (Start start : level.getItems(Start.class)) { // TODO test me
            if (start.isOwnedBy(hero)) {
                start.setOwner(null);
            }
        }
    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    public List<Player> getPlayers() {
        return new LinkedList(players);
    }

    @Override
    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    @Override
    public boolean isFree() {
        return nothingChanged() && freeBases() > 0;
    }

    private boolean nothingChanged() {
        return nothingChanged;
    }

    @Override
    public int freeBases() {
        if (isMultiplayer) {
            return (getFreeBase() == null) ? 0 : allBases() - players.size();
        } else {
            return (players.isEmpty()) ? allBases() : 0;
        }
    }

    @Override
    public int allBases() {
        return isMultiplayer ? 4 : 1;
    }

    @Override
    public int getRoundTicks() {
        if (!data.roundLimitedInTime()) {
            return SettingsWrapper.UNLIMITED;
        }
        return roundTicks;
    }

    @Override
    public int getViewSize() {
        return level.getViewSize();
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("players", players());
                put("id", id());
                put("isMultiple", isMultiplayer);
                put("losers", Player.lg(losers));
                put("waitingOthers", isWaitingOthers());
                put("roundTicks", roundTicks);
                put("level", printer());
            }};
        }

        private List<String> players() {
            return Player.lg(Expansion.this.players);
        }

        public PrinterData printer() {
            try {
                return Expansion.this.players.get(0).getPrinter().print();
            } catch (Exception e) {
                return null;
            }
        }

        public String id() {
            return "E@" + Integer.toHexString(Expansion.this.hashCode());
        }
    }

    public LogState lg = new LogState();

    @Override
    public String id() {
        return lg.id();
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return Expansion.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return null; // because layeredReader() implemented here
            }
        };
    }

    public LayeredBoardReader layeredReader() {
        return new LayeredBoardReader() {
            @Override
            public int size() {
                return Expansion.this.size();
            }

            @Override
            public int viewSize() {
                int viewSize = Expansion.this.level.getViewSize();
                return (viewSize == -1) ? size() : viewSize;
            }

            @Override
            public BiFunction<Integer, Integer, State> elements() {
                Cell[] cells = Expansion.this.getCurrentLevel().getCells();
                return (index, layer) -> {
                    if (layer == 2) {
                        return new ForcesState(cells[index].getItem(HeroForces.class));
                    } else {
                        return cells[index].getItem(layer);
                    }
                };
            }

            @Override
            public Point viewCenter(Object player) {
                return ((Player)player).getHero().getPosition();
            }

            @Override
            public Object[] itemsInSameCell(State item, int layer) {
                if (item instanceof Item) {
                    // TODO передавать дальше int layer и вообще сделать как в icancode от 2020-04-20
                    return ((Item) item).getItemsInSameCell().toArray();
                } else {
                    return new Object[0];
                }
            }
        };
    }

    @Override
    public long ticker() {
        return ticker.get();
    }

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(lg.json());
    }

}
