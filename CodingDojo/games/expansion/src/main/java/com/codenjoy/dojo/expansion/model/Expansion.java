package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.expansion.services.Event;
import com.codenjoy.dojo.expansion.services.GameSettings;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.printer.state.State;
import com.codenjoy.dojo.utils.JsonUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;

import static com.codenjoy.dojo.expansion.services.Event.Type.WIN;

public class Expansion implements Tickable, IField {

    public static Event WIN_MULTIPLE;
    public static Event DRAW_MULTIPLE;
    public static final Event WIN_SINGLE = new Event(Event.Type.WIN, 0);
    public static final Event LOSE = new Event(Event.Type.LOSE, 0);

    private static final Logger log = LoggerFactory.getLogger(Expansion.class);

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;
    private GameLogger gameLogger;

    private Level level;
    private final Ticker ticker;
    private final Dice dice;
    private GameSettings settings;

    private boolean isMultiplayer;
    private boolean nothingChanged;

    private List<Player> players;
    private int roundTicks;

    public Expansion(Level level, Ticker ticker, Dice dice, GameLogger gameLogger, boolean multiple, GameSettings settings) {
        this.level = level;
        this.ticker = ticker;
        this.dice = dice;
        this.settings = settings;
        WIN_MULTIPLE = new Event(WIN, settings.winScore());
        DRAW_MULTIPLE = new Event(WIN, settings.drawScore());
        level.field(this);
        isMultiplayer = multiple;
        players = new LinkedList();
        this.gameLogger = gameLogger;
        cleanAfterGame();
    }

    private void cleanAfterGame() {
        roundTicks = 0;
        nothingChanged = true;
        if (isMultiplayer) {
            gameLogger.start(this);
        }
    }

    @Override
    public void tick() {
        if (log.isDebugEnabled()) {
            log.debug("Expansion {} started tick", lg.id());
        }

        ticker.tick();

        roundTicks++;

        if (log.isDebugEnabled()) {
            log.debug("Expansion processing board calculations. " +
                            "State before processing {}",
                    this);
        }

        if (isMultiplayer) {
            gameLogger.logState();
        }

        if (isMultiplayer) {
            Player winner = null;
            for (Player player : players) {
                Hero hero = player.getHero();

                Event status = checkStatus(player, hero);
                if (status != null) {
                    player.event(status);
                }

                if (Arrays.asList(WIN_MULTIPLE, DRAW_MULTIPLE).contains(status)) {
                    winner = player;
                }
            }

            if (winner != null) {
                allHeroesDie();
                return;
            }
        }

        if (settings.roundLimitedInTime()) {
            if (roundTicks >= settings.roundTicks()) {
                for (Player player : players) {
                    player.event(DRAW_MULTIPLE);
                }
                allHeroesDie();

                if (log.isDebugEnabled()) {
                    log.debug("Expansion round is out. All players will be removed! {}",
                            this);
                }

                return;
            }
        }

        // there player level be changed
        for (Player player : players.toArray(new Player[0])) {
            player.getHero().tick();
        }

        if (!players.isEmpty()) {
            attack();

            for (Tickable item : level.items(Tickable.class)) {
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

        if (log.isDebugEnabled()) {
            log.debug("Expansion finished tick. " +
                    "State after processing {}", toString());
        }
    }

    private void allHeroesDie() {
        for (Player player : players) {
            player.getHero().die();
            player.destroyHero();
        }
        cleanAfterGame();
    }

    private void attack() {
        if (log.isDebugEnabled()) {
            countChecker.before();
        }

        for (Cell cell : level.cellsWith(HeroForces.class)) {
            List<HeroForces> forces = cell.getItems(HeroForces.class);
            if (forces.size() <= 1) continue;

            nothingChanged &= !settings.attack().calculate(forces);
        }

        if (log.isDebugEnabled()) {
            log.debug("During call attack() method for game {} found this " +
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
            return Arrays.asList(level.cells()).stream()
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
        if (log.isDebugEnabled()) {
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

        if (log.isDebugEnabled()) {
            log.debug("During call increase() method for hero {} found " +
                            "this forces count delta {} (hero can only {}!)",
                    hero.lg.id(),
                    countChecker.after(),
                    hero.getForcesPerTick());
        }
    }

    @Override
    public void move(Hero hero, List<ForcesMoves> movements) {
        if (log.isDebugEnabled()) {
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

        if (log.isDebugEnabled()) {
            if (countChecker.after() != 0) {
                System.out.println();
            }
            log.debug("During call move() method for hero {} found this " +
                            "forces count delta {} (it should be 0!)",
                    hero.lg.id(),
                    countChecker.after());
        }
    }

    private Event checkStatus(Player player, Hero hero) {
        if (players.size() == 1) {
            List<Cell> freeCells = level.cellsWith(
                    cell -> cell.getItems(HeroForces.class).isEmpty() &&
                            cell.isPassable() && cell.getItem(Hole.class) == null
            );
            if (freeCells.isEmpty()) {
                return DRAW_MULTIPLE;
            }
            return null;
        }

        List<HeroForces> allForces = level.items(HeroForces.class);
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
            player.getHero().die();
            return LOSE;
        }
        return null;
    }

    @Override
    public int size() {
        return level.size();
    }

    private boolean isBarrier(int x, int y) {
        return level.isBarrier(x, y);
    }

    @Override
    public Start getBaseOf(Hero hero) {
        List<Start> bases = level.items(Start.class);

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
        List<Start> bases = level.items(Start.class);

        Collections.sort(bases, Comparator.comparingInt(Start::index));

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

        Cell cell = level.cell(x, y);
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
        Cell cell = level.cell(x, y);
        HeroForces force = cell.getItem(HeroForces.class);
        if (force != null && force.itsMe(hero)) {
            force.removeFromCell();
        }
    }

    private int leaveForces(Hero hero, int x, int y, int count) {
        Cell cell = level.cell(x, y);

        HeroForces force = getHeroForces(hero, cell);
        if (force == null) {
            return 0;
        }

        return force.leave(count, settings.leaveForceCount());
    }

    private int countForces(Hero hero, int x, int y) {
        Cell cell = level.cell(x, y);

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

        for (Gold gold : level.items(Gold.class)) {
            gold.reset();
        }
    }

    @Override
    public int totalRegions(){
        return level.cellsWith(cell -> cell.isPassable()
                && cell.getItem(Hole.class) == null).size();
    }

    @Override
    public int regionsCount(Hero hero) {
        return level.cellsWith(cell -> cell.busy(hero)).size();
    }

    // TODO move to RoundField when time is right
    @Override
    public void newGame(Player player) {
        if (players.contains(player)) {
            remove(player);
        }
        players.add(player);
        onAdd(player);
    }

    protected void onAdd(Player player) {
        player.newHero(this);
        if (isMultiplayer) {
            gameLogger.register(player);
        }
    }

    protected void onRemove(Player player) {
        player.destroyHero();
        if (players.isEmpty()) {
            cleanAfterGame();
        }
    }

    // TODO move to RoundField when time is right
    @Override
    public void remove(Player player) {
        players.remove(player);
        onRemove(player);
    }

    @Override
    public void removeFromCell(Hero hero) {
        for (HeroForces forces : level.items(HeroForces.class)) {
            if (forces.itsMe(hero)) {
                forces.removeFromCell();
            }
        }
        for (Start start : level.items(Start.class)) { // TODO test me
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
        return new LinkedList<>(players);
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
        if (!settings.roundLimitedInTime()) {
            return GameSettings.UNLIMITED;
        }
        return roundTicks;
    }

    @Override
    public int getViewSize() {
        return level.viewSize();
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("players", players());
                put("id", id());
                put("isMultiple", isMultiplayer);
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

    public LayeredBoardReader<Player> layeredReader() {
        return new LayeredBoardReader<>() {
            @Override
            public int size() {
                return Expansion.this.size();
            }

            @Override
            public int viewSize() {
                int viewSize = Expansion.this.level.viewSize();
                return (viewSize == -1) ? size() : viewSize;
            }

            @Override
            public BiFunction<Integer, Integer, State> elements() {
                Cell[] cells = Expansion.this.getCurrentLevel().cells();
                return (index, layer) -> {
                    if (layer == 2) {
                        return new ForcesState(cells[index].getItem(HeroForces.class));
                    } else {
                        return cells[index].getItem(layer);
                    }
                };
            }

            @Override
            public Point viewCenter(Player player) {
                return player.getHero().getPosition();
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

    @Override
    public GameSettings settings() {
        return settings;
    }
}
