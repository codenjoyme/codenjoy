package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:59
 */
public class LevelImpl implements Level {

    private LengthToXY xy;
    private final GameSettings settings;
    private String map;
    private Walls walls;
    private Parameter<Integer> bombs;
    private Parameter<Integer> size;

    private List<PointImpl> destroyedWalls;
    private List<Bomb> destroyedBombs;
    private List<Bomb> listBombs;
    private List<Blast> listBlast;
    private List<Player> listPlayers;
    private List<Player> botPlayers;
    private List<Player> nonBotPlayers;
    private List<MeatChopper> choppers;

    private OriginalWalls originalWalls;
    private EatSpaceWalls eatWalls;
    private MeatChoppers meatChoppers;

    private String mapName;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    public LevelImpl(String map, GameSettings settings) {
        this.map = map;
        this.size = v((int) Math.sqrt(map.length()));
        this.xy = new LengthToXY(this.size.getValue());
        this.bombs = v(getPointsOf(Elements.BOOM).size());
        this.destroyedWalls = new LinkedList<PointImpl>();
        this.destroyedBombs = new LinkedList<Bomb>();
        this.listBombs = new LinkedList<Bomb>();
        this.listBlast = new LinkedList<Blast>();
        this.listPlayers = new LinkedList<Player>();
        this.botPlayers = new LinkedList<>();
        this.nonBotPlayers = new LinkedList<>();
        this.choppers = new LinkedList<>();
        this.settings = settings;
        this.mapName = settings.getMapFile().getValue();

        //выставляем стены
        List<Point> points = getPointsOf(Elements.WALL);
        this.originalWalls = new OriginalWalls(points);

        //Выставляем уничтожаемые стены
        points = getPointsOf(Elements.DESTROYABLE_WALL);
        this.eatWalls = new EatSpaceWalls(originalWalls, this, points, new RandomDice());

        //Выставляем мясников
        points = getPointsOf(Elements.MEAT_CHOPPER);
        this.meatChoppers = new MeatChoppers(eatWalls, this, points, new RandomDice());

        this.walls = originalWalls;

    }

    // Map
    @Override
    public Parameter<Integer> getSize() {
        return v((int) Math.sqrt(map.length()));
    }

    //Bombs Section
    @Override
    public Parameter<Integer> bombsCount() {
        return this.bombs;
    }

    @Override
    public Parameter<Integer> bombsPower() {
        return v(3);
    }

    @Override
    public int size() {
        return size.getValue();
    }

    @Override
    public List<Bomb> getBombs() {
        return this.listBombs;
    }

    @Override
    public List<Bomb> getBombs(HeroImpl bomberman) {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    @Override
    public List<Bomb> getDestroyedBombs() {
        return this.destroyedBombs;
    }

    @Override
    public void prepareChoppers() {
        this.choppers = walls.subList(MeatChopper.class);
    }

    @Override
    public void cleanupChoppers() {
        this.choppers.clear();
    }

    @Override
    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        for (Player bomberman : getPlayers()) {
            if (bomberman.getBomberman().itsMe(new PointImpl(x, y))) {
                return true;
            }
        }
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMe(x, y)) {
                return true;
            }
        }
        for (Wall wall : getWalls()) {
            if (wall instanceof MeatChopper && !isWithMeatChopper) {
                continue;
            }
            if (wall.itsMe(x, y)) {
                return true;
            }
        }
        return x < 0 || y < 0 || x > size.getValue() - 1 || y > size.getValue() - 1;
    }

    @Override
    public boolean isBarrier(Point botPos, int newX, int newY, boolean isWithMeatChopper) {
        if (botPos != null && isAnotherBomberman(botPos, newX, newY)) {
            return false;
        }
        if (botPos != null && isMeetChopper(PointImpl.pt(newX, newY))) {
            return true;
        }
        for (Hero bomberman : getBombermans()) {
            if (bomberman.itsMe(new PointImpl(newX, newY))) {
                return true;
            }
        }
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMe(newX, newY)) {
                return true;
            }
        }
        for (Wall wall : walls) {
            if (wall instanceof MeatChopper && !isWithMeatChopper) {
                continue;
            }
            if (wall.itsMe(newX, newY)) {
                return true;
            }
        }
        return newX < 0 || newY < 0 || newX > size() - 1 || newY > size() - 1;
    }

    @Override
    public boolean isAnotherBomberman(Point currentPos, int newX, int newY) {
        for (Hero bomberman : getBombermans()) {
            if (bomberman.itsMe(currentPos)) {
                continue;
            }
            if (bomberman.itsMe(PointImpl.pt(newX, newY))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMeetChopper(Point pos) {
        for (MeatChopper chopper : choppers) {
            if (chopper.itsMe(pos)) {
                return true;
            }
        }
        return false;
    }

    //Blast Section
    @Override
    public List<Blast> getBlasts() {
        return this.listBlast;
    }

    @Override
    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            getBombs().add(bomb);
        }
    }

    @Override
    public void removeBomb(Bomb bomb) {
        this.destroyedBombs.add(bomb);
    }

    @Override
    public List<Player> getPlayers() {
        return this.listPlayers;
    }

    @Override
    public List<Player> getBotPlayers() {
        return this.botPlayers;
    }

    @Override
    public List<Player> getNonBotPlayers() {
        return this.nonBotPlayers;
    }

    //Wall Section
    @Override
    public Walls getWalls() {
        return this.walls;
    }

    @Override
    public List<PointImpl> getDestroyedWalls() {
        return this.destroyedWalls;
    }

    @Override
    public List<Hero> getBombermans() {
        List<Hero> result = new LinkedList<Hero>();
        for (Player player : this.listPlayers) {
            result.add(player.getBomberman());
        }
        return result;
    }

    @Override
    public void remove(Player player) {
        this.listPlayers.remove(player);
        (player.isBot() ? botPlayers : nonBotPlayers).remove(player);
    }

    // Private methods
    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    private boolean existAtPlace(int x, int y) {
        for (Bomb bomb : getBombs()) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private void tactAllPlayers() {
        for (Player player : getPlayers()) {
            player.getBomberman().apply();
        }
    }

    private void removeBlasts() {
        getBlasts().clear();
        for (PointImpl pt : getDestroyedWalls()) {
            getWalls().destroy(pt.getX(), pt.getY());
        }
        getDestroyedWalls().clear();
    }

    private void meatChopperEatBombermans() {
        for (MeatChopper chopper : getWalls().subList(MeatChopper.class)) {
            for (Player player : getPlayers()) {
                Hero bomberman = player.getBomberman();
                if (bomberman.isAlive() && chopper.itsMe(bomberman) && !bomberman.isBot()) {
                    player.event(Events.KILL_BOMBERMAN);
                }
            }
        }

        botPlayersEatPlayers();
    }

    private void botPlayersEatPlayers() {
        for (Player botPlayer : getBotPlayers()) {
            for (Player player : getNonBotPlayers()) {
                Hero bomberman = player.getBomberman();
                if (!bomberman.isBot() && bomberman.isAlive() && botPlayer.getBomberman().itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
                    botPlayer.event(Events.KILL_OTHER_BOMBERMAN);
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : getBombs()) {
            bomb.tick();
        }

        for (Bomb bomb : getDestroyedBombs()) {
            getBombs().remove(bomb);

            List<Blast> blast = makeBlast(bomb);
            killAllNear(blast, bomb);
            getBlasts().addAll(blast);
        }
        getDestroyedBombs().clear();
    }

    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = (List) getWalls().subList(Wall.class);
        barriers.addAll(getBombermans());

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast : blasts) {
            if (getWalls().itsMe(blast.getX(), blast.getY())) {
                getDestroyedWalls().add(blast);

                Wall wall = getWalls().get(blast.getX(), blast.getY());
                wallDestroyed(wall, blast);
            }
        }
        for (Blast blast : blasts) {
            for (Player dead : getPlayers()) {
                if (dead.getBomberman().itsMe(blast)) {
                    dead.event(Events.KILL_BOMBERMAN);

                    for (Player bombOwner : getPlayers()) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getBomberman())) {
                            bombOwner.event(Events.KILL_OTHER_BOMBERMAN);
                        }
                    }
                }
            }
        }
    }

    private void wallDestroyed(Wall wall, Blast blast) {
        for (Player player : getPlayers()) {
            if (blast.itsMine(player.getBomberman())) {
                if (wall instanceof MeatChopper) {
                    player.event(Events.KILL_MEAT_CHOPPER);
                } else if (wall instanceof DestroyWall) {
                    player.event(Events.KILL_DESTROY_WALL);
                }
            }
        }
    }

    private void mapReload()
    {
        if (settings.getMapFile().changed())
        {
            File file = new File(String.format("%s.map", settings.getMapFile().getValue()));
            //load data
            try (Scanner scanner =  new Scanner(file, ENCODING.name())){
                String map = "";
                while (scanner.hasNextLine()){
                    map = map + scanner.nextLine().trim();
                }
                this.map = map;
                this.size = v((int) Math.sqrt(map.length()));
                this.xy = new LengthToXY(this.size.getValue());
                this.bombs = v(getPointsOf(Elements.BOOM).size());
                this.destroyedWalls.clear();
                this.destroyedBombs.clear();
                this.listBombs.clear();
                this.listBlast.clear();
                for (Player p:this.listPlayers){
                    p.event(Events.KILL_BOMBERMAN);
                }
                for (PointImpl pt : this.originalWalls) {
                    this.originalWalls.destroy(pt.getX(), pt.getY());
                }
                loadMap();
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден: " + file.getAbsolutePath());
            }
            settings.getMapFile().changesReacted();
        }
    }

    private void loadMap()
    {
        //выставляем стены
        List<Point> points = getPointsOf(Elements.WALL);
        this.originalWalls = new OriginalWalls(points);

        //Выставляем уничтожаемые стены
        points = getPointsOf(Elements.DESTROYABLE_WALL);
        this.eatWalls = new EatSpaceWalls(originalWalls, this, points, new RandomDice());

        //Выставляем мясников
        points = getPointsOf(Elements.MEAT_CHOPPER);
        this.meatChoppers = new MeatChoppers(eatWalls, this, points, new RandomDice());

        this.walls = originalWalls;
    }

    @Override
    public void tick() {
        prepareChoppers();
        removeBlasts();
        tactAllPlayers();
        meatChopperEatBombermans();

        this.walls.tick();
        this.originalWalls.tick();
        this.eatWalls.tick();
        this.meatChoppers.tick();

        meatChopperEatBombermans();
        tactAllBombs();

        mapReload();
    }
}
