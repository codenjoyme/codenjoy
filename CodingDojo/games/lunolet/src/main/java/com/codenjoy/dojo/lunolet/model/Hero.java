package com.codenjoy.dojo.lunolet.model;

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


import com.codenjoy.dojo.lunolet.services.Events;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hero extends PlayerHero<Field> {

    private Player player;
    private Simulator simulator;
    private Point2D.Double target;
    private Point2D.Double targetPoint1;
    private Point2D.Double targetPoint2;
    private double levelLeft;
    private double levelRight;
    private boolean isAlive;

    public Hero(Player player) {
        this.player = player;

        simulator = new Simulator();
        isAlive = true;
    }

    public void init(Level level) {
        simulator.reset();
        simulator.DryMass = level.DryMass;
        simulator.setRelief(level.Relief);
        simulator.setVesselStatus(level.VesselStatus);

        // find out target point using TargetX
        List<Point2D.Double> relief = level.Relief;
        double targetX = level.TargetX;
        for (int i = 0; i < relief.size() - 1; i++) {
            Point2D.Double pt1 = relief.get(i);
            Point2D.Double pt2 = relief.get(i + 1);
            if (pt1.x < targetX && pt2.x >= targetX) {
                double targetY;
                if (Math.abs(pt2.x - targetX) < 1e-5)
                    targetY = pt2.y;
                else
                    targetY = ((targetX - pt1.x) / (pt2.x - pt1.x) * (pt2.y - pt1.y) + pt1.y);

                target = new Point2D.Double(targetX, targetY);

                if (pt1.x <= pt2.x) {
                    targetPoint1 = pt1;
                    targetPoint2 = pt2;
                } else {
                    targetPoint1 = pt2;
                    targetPoint2 = pt1;
                }

                break;
            }
        }
        //TODO: if target == null then something wrong with this level

        // find out box left/right margins
        levelLeft = 0.0;
        levelRight = 0.0;
        for (int i = 0; i < relief.size(); i++) {
            Point2D.Double pt1 = relief.get(i);
            if (pt1.x < levelLeft)
                levelLeft = pt1.x;
            if (pt1.x > levelRight)
                levelRight = pt1.x;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public List<Point2D.Double> getLevelRelief() {
        return simulator.Relief;
    }

    public List<Point2D.Double> getVesselHistory() {
        return simulator.History;
    }

    public VesselStatus getVesselStatus() {
        return simulator.Status;
    }

    public double getLastAngle() {
        return simulator.LastAngle;
    }

    public Point2D.Double getTarget() {
        return target;
    }

    @Override
    public void tick() {
        //do nothing: no actions driven by real time

        if (!simulator.Status.isNotFinalState())
            isAlive = false;
    }

    @Override
    public void down() {
        simulate(180.0, 0.1, 1.0);
    }

    @Override
    public void up() {
        simulate(0.0, 0.2, 1.0);
    }

    @Override
    public void left() {
        simulate(-90.0, 0.1, 1.0);
    }

    @Override
    public void right() {
        simulate(90.0, 0.1, 1.0);
    }

    @Override
    public void act(int... ints) {
        double duration = 1.0;
        if (ints.length > 0 && ints[0] > 0)
            duration = ints[0];
        simulate(0.0, 0.0, duration); // free flight
    }

    @Override
    public void message(String s) {
        String command = s.toLowerCase();

        if (command.equals("nothing")) {
            simulate(0.0, 0.0, 1.0); // free flight 1 second
            return;
        }

        if (command.equals("reset") || command.equals("die")) {
            isAlive = false; // player decided to die
            return;
        }

        Pattern patternGo = Pattern.compile(
                "go\\s*(-?[\\d\\.]+)[,\\s]\\s*(-?[\\d\\.]+)[,\\s]\\s*(-?[\\d\\.]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternGo.matcher(command);
        if (matcher.matches()) {
            double angle = Double.parseDouble(matcher.group(1));
            double mass = Double.parseDouble(matcher.group(2));
            double duration = Double.parseDouble(matcher.group(3));
            if (duration <= 0.0 || mass < 0.0)
                return;
            simulate(angle, mass, duration);
            return;
        }

        //TODO
    }

    private void simulate(double angle, double mass, double duration) {
        if (simulator.Status.isNotFinalState()) {
            simulator.simulate(angle, mass, duration);

            // check if we're landed on the proper segment
            if (simulator.Status.State == VesselState.LANDED &&
                    targetPoint1 != null && targetPoint2 != null) {
                double x = simulator.Status.X;
                //double y = simulator.Status.Y;
                if (x < targetPoint1.x && x < targetPoint2.x ||
                        targetPoint1.x < x && targetPoint2.x < x) {
                    simulator.Status.State = VesselState.CRASHED;
                }
            }

            // check if we're out of the box
            if (simulator.Status.State == VesselState.FLIGHT &&
                    (simulator.Status.X < levelLeft || simulator.Status.X > levelRight)) {
                simulator.Status.State = VesselState.CRASHED;
            }

            // trigger player events
            if (simulator.Status.State == VesselState.LANDED) {
                player.event(Events.LANDED);
            } else if (simulator.Status.State == VesselState.CRASHED) {
                player.event(Events.CRASHED);
            }
        }
    }

    public void die() {
        isAlive = false;
    }
}
