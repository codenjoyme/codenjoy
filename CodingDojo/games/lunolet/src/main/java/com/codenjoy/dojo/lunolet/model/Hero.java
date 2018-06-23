package com.codenjoy.dojo.lunolet.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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
import com.codenjoy.dojo.services.*;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hero implements Joystick, Tickable {

    private Player player;
    private Level level;
    private Simulator simulator;

    public Hero(Player player) {
        this.player = player;

        simulator = new Simulator();
    }

    public void init(Level level) {
        this.level = level;

        simulator.reset();
        simulator.DryMass = level.DryMass;
        simulator.setRelief(level.Relief);
        simulator.setVesselStatus(level.VesselStatus);
    }

    public boolean isAlive() {
        return simulator.Status.isAlive();
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

    @Override
    public void tick() {
        //do nothing: no actions driven by real time
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

        Pattern patternGo = Pattern.compile(
                "go\\s*(-?[\\d\\.]+)[,\\s](-?[\\d\\.]+)[,\\s](-?[\\d\\.]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternGo.matcher(command);
        if (matcher.matches()) {
            double angle = Double.parseDouble(matcher.group(1));
            double mass = Double.parseDouble(matcher.group(2));
            double duration = Double.parseDouble(matcher.group(3));
            simulate(angle, mass, duration);
            return;
        }

        //TODO
    }

    private void simulate(double angle, double mass, double duration) {
        if (simulator.Status.isAlive()) {
            simulator.simulate(angle, mass, duration);

            if (simulator.Status.State == VesselState.LANDED) {
                player.event(Events.LANDED);
            }
        }
    }
}
