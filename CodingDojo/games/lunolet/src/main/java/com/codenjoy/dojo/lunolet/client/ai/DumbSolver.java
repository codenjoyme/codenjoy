package com.codenjoy.dojo.lunolet.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.lunolet.client.Board;
import com.codenjoy.dojo.lunolet.model.VesselState;
import com.codenjoy.dojo.lunolet.utility.PrintLevels;

import java.awt.geom.Point2D;

public class DumbSolver implements Solver<Board> {

    private boolean shouldBrake = false;
    private double mass;
    private double angle;

    @Override
    public String get(Board board) {
        if(Math.abs(board.getHSpeed()) < 1e-5 && board.getVSpeed() > 5)
            return "message('go 0, 0, 1')";

        mass = 0.0;
        angle = 0.0;
        double hMass = 0.0;
        double vMass = 0.0;

        // go UP on start
        if (board.getState() == VesselState.START) {
            vMass = calculateFuelMassForGoingUp(board, 4.62);
            calculateTotalMassAndAngle(hMass, vMass);
            mass = preventVertigo(mass, board);
            return PrintLevels.stringFormat("message('go %f, %f, 1')", angle, mass);
        }

        boolean closeToTarget = closeToTarget(board, 2.0);
        boolean hSpeedIsZero = hSpeedIsZero(board);

        if (highObstacleOnWay(board)) {
            vMass = calculateFuelMassForGoingUp(board, 4.62);
            calculateTotalMassAndAngle(hMass, vMass);
            mass = preventVertigo(mass, board);
            return PrintLevels.stringFormat("message('go %f, %f, 1')", angle, mass);
        }

        if (fallDownTooFastOrLowFlying(board)) {
            vMass = calculateFuelMassForGoingUp(board, 4.62);
        }

        Point2D.Double point = board.getPoint();
        Point2D.Double target = board.getTarget();

        if (closeToTarget) {
            if (hSpeedIsZero) {
                if (vMass == 0.0)
                    vMass = -0.1;
            } else {
                // braking
                hMass = ((250 + board.getFuelMass()) * board.getHSpeed()) / (3660 + board.getHSpeed());
                shouldBrake = false;
            }
        } else {
            hMass = Math.abs(target.x - point.x) / 100.0;
        }

        double preferredDirection = (target.x - point.x) - board.getHSpeed() * Math.abs(board.getHSpeed());
        hMass = preferredDirection > 0 ? hMass : -hMass;

        calculateTotalMassAndAngle(hMass, vMass);

        mass = preventVertigo(mass, board);
        return PrintLevels.stringFormat("message('go %f, %f, 1')", angle, mass);
    }

    private double preventVertigo(double mass, Board board) {
        double maxMass = calculateFuelMassForGoingUp(board, 29.43);
        return Math.min(maxMass, mass);
    }

    private double calculateFuelMassForGoingUp(Board board, double acceleration) {
        return (acceleration) * (250.0 + board.getFuelMass()) / 3600.0;
    }

    private void calculateTotalMassAndAngle(double hMass, double vMass) {
        mass = Math.sqrt(hMass * hMass + vMass * vMass);
        angle = Math.asin(Math.abs(vMass) / mass) / (Math.PI / 180.0);
        if (hMass < 0) {
            angle = 180 - angle;
        }
        if (vMass < 0) {
            angle = -angle;
        }
        angle = 90 - angle;
        //System.out.println("2 angle:" + (angle) + /*", mass:" + mass + */", hMass:" + hMass + ", vMass:" + vMass);
    }

    private boolean fallDownTooFastOrLowFlying(Board board) {
        Point2D.Double target = board.getTarget();
        Point2D.Double point = board.getPoint();
        return board.getVSpeed() < -1.5 || (Math.abs(target.x - point.x) > 10 && Math.abs(target.y - point.y) < 20);
    }

    private boolean highObstacleOnWay(Board board) {
        Point2D.Double target = board.getTarget();
        Point2D.Double point = board.getPoint();
        Point2D.Double start = point.x < target.x ? point : target;
        Point2D.Double end = point.x < target.x ? target : point;

        for (Point2D.Double p : board.getRelief()) {
            if (p.x > start.x && p.x < end.x && p.y + 20.0 > point.y) {
                return true;
            }
        }
        return false;
    }

    private boolean closeToTarget(Board board, double distance) {
        double me = board.getPoint().x;
        double t = board.getTarget().x;
        double nextPositionOnFlight = me + board.getHSpeed() * 1;
        boolean close = Math.abs(me - t) < distance;
        boolean isCrossTarget = (me - t) * (nextPositionOnFlight - t) < 0;
        return close || isCrossTarget;
    }

    private boolean hSpeedIsZero(Board board) {
        return Math.abs(board.getHSpeed()) < 0.001;
    }
}
