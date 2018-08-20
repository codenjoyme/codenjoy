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


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class Simulator {

    double Eps = 1e-5;
    double GravityAccel = 1.62;
    double DryMass = 250;
    double ExhaustSpeed = 3660;
    double AccelLimit = 3 * 9.81;
    double LandSpeedLimit = 5.0;

    public VesselStatus Status;

    public List<Point2D.Double> Relief;

    public List<Point2D.Double> History;

    public double LastAngle;

    private double unconsciousTime;

    public Simulator() {
        Status = new VesselStatus();
        Relief = new LinkedList<Point2D.Double>();
        History = new LinkedList<Point2D.Double>();

        Relief.add(new Point2D.Double(-10000, 0));
        Relief.add(new Point2D.Double(10000, 0));

        GravityAccel = 1.62;
        DryMass = 250;
        ExhaustSpeed = 3660;
        AccelLimit = 3 * 9.81;
        LandSpeedLimit = 5.0;

        reset();
    }

    public void reset() {
        Status.Time = 0;
        Status.VSpeed = 0;
        Status.HSpeed = 0;
        Status.Y = 0;
        Status.X = 0;
        Status.FuelMass = 50;
        Status.State = VesselState.START;

        unconsciousTime = 0.0;

        History.clear();

        LastAngle = 0.0;
    }

    public void setVesselStatus(VesselStatus status) {
        if (status == null)
            throw new IllegalArgumentException("status should not be null.");

        Status.Time = 0;
        Status.VSpeed = status.VSpeed;
        Status.HSpeed = status.HSpeed;
        Status.X = status.X;
        Status.Y = status.Y;
        Status.FuelMass = status.FuelMass;
        Status.State = status.State;

        History.clear();
    }

    public void setRelief(List<Point2D.Double> relief) {
        if (relief == null || relief.size() < 2)
            throw new IllegalArgumentException("relief should be non-null, at least two points.");

        Relief.clear();
        Relief.addAll(relief);
    }

    /**
     * Simulate given lunolet maneuver.
     *
     * @param angle
     * @param mass
     * @param duration
     * @throws IllegalArgumentException if mass less than 0 or duration not a positive value
     */
    public void simulate(double angle, double mass, double duration) {
        if (mass < 0.0)
            throw new IllegalArgumentException("mass should be positive number or zero.");
        if (duration <= 0.0)
            throw new IllegalArgumentException("duration should be positive number.");

        if (Status.State == VesselState.CRASHED || Status.State == VesselState.LANDED)
            return;

        // the History shows the last simulate() call history only
        History.clear();
        History.add(Status.getPoint());

        simulateBlock(angle, mass, duration);

        if (unconsciousTime > 0.0) {
            simulateBlock(0, 0, unconsciousTime);
            unconsciousTime = 0.0;
        }

        LastAngle = angle;
    }

    private void simulateBlock(double angle, double mass, double duration) {
        if (duration <= 1.0) {
            simulateStep(angle, mass, duration);
        } else {
            int steps = (int) Math.ceil(duration);
            for (int s = 0; s < steps; s++) {
                double stepDuration = 1.0;
                if (s == steps - 1 && (duration % 1) > 0.0)
                    stepDuration = (duration % 1);
                double stepMass = mass * stepDuration / duration;

                simulateStep(angle, stepMass, stepDuration);

                if (Status.State == VesselState.CRASHED || Status.State == VesselState.LANDED)
                    break;
            }
        }
    }

    private void simulateStep(double angle, double mass, double duration) {
        if (mass > Status.FuelMass) {
            if (Status.FuelMass < Eps) { // Out of fuel
                mass = 0;
            } else {
                duration *= Status.FuelMass / mass;
                mass = Status.FuelMass;
            }
        }

        if (mass < Eps) {
            flight(0, duration, 0, angle);
            Status.Consumption = 0;
        } else {
            double consumption = mass / duration;
            double accel = consumption * ExhaustSpeed / (DryMass + Status.FuelMass);

            flight(mass, duration, accel, angle);
            Status.Consumption = consumption;

            // Check for overload; if overload then pilot unconscious, free flight
            if (Status.State != VesselState.CRASHED && Status.State != VesselState.LANDED &&
                    accel > AccelLimit) {
                unconsciousTime = accel - AccelLimit;
            }
        }
    }

    private void flight(double mass, double duration, double accel, double angle) {

        VesselStatus newstatus = advance(Status, mass, duration, accel, angle);

        // Check if Status/newstatus line intersects relief
        Point2D.Double point1 = Status.getPoint();
        Point2D.Double point2 = newstatus.getPoint();
        Line2D.Double line1 = new Line2D.Double(point1, point2);
        Line2D.Double line2 = null;
        Point2D.Double pointHit = null;
        for (int i = 0; i < Relief.size() - 1; i++) {
            line2 = new Line2D.Double(Relief.get(i), Relief.get(i + 1));

            pointHit = LineIntersection.findIntersection(line1, line2);
            if (pointHit != null)
                break;
        }

        if (pointHit != null) {
            double distance = point1.distance(pointHit);
            if (distance > Eps) // Hit point is NOT almost the start point
            {
                double speed = newstatus.getSpeed();
                newstatus.State = speed < LandSpeedLimit ? VesselState.LANDED : VesselState.CRASHED;

                double distanceFull = point1.distance(point2);
                if (distanceFull <= Eps)
                    return; // End point is almost the start point

                double distance2 = point2.distance(pointHit);
                if (distance2 > Eps) // End point is NOT almost the hit point
                {
                    double durationMin = 0.0;
                    double durationMax = duration;
                    double durationMiddle = duration / 2.0;
                    int count = 0;
                    for (; count < 18; count++) {
                        durationMiddle = (durationMin + durationMax) / 2.0;
                        double massMiddle = mass * durationMiddle / duration;
                        VesselStatus newstatusprobe = advance(Status, massMiddle, durationMiddle, accel, angle);
                        point2 = newstatusprobe.getPoint();
                        Line2D.Double lineprobe = new Line2D.Double(point1, point2);
                        pointHit = LineIntersection.findIntersection(lineprobe, line2);
                        if (pointHit == null) {
                            durationMin = durationMiddle;
                        } else {
                            distance2 = point2.distance(pointHit);
                            if (distance2 <= Eps)
                                break;
                            durationMax = durationMiddle;
                        }
                    }
                    //Console.WriteLine($"advance() landing loop count: {count}");//DEBUG

                    if (durationMiddle > Eps) {
                        double masscorr = mass == 0.0 ? 0.0 : mass * durationMiddle / duration;
                        newstatus = advance(Status, masscorr, durationMiddle, accel, angle);
                        speed = newstatus.getSpeed();
                        newstatus.State = speed < LandSpeedLimit ? VesselState.LANDED : VesselState.CRASHED;
                    }
                }
            }
        }

        // Special case: on take-off make sure the vessel was lifted up
        if (Status.State == VesselState.START && (newstatus.Y - Status.Y) < Eps)
            return;

        Status = newstatus;

        History.add(newstatus.getPoint());
    }

    private VesselStatus advance(VesselStatus status, double mass, double duration, double accel, double angle) {

        double anglerad = Math.PI * angle / 180.0;
        double haccel = accel * Math.sin(anglerad);
        double vaccel = accel * Math.cos(anglerad) - GravityAccel;

        double hspeedAfter = status.HSpeed + duration * haccel;
        double vspeedAfter = status.VSpeed + duration * vaccel;

        double avgHSpeed = (status.HSpeed + hspeedAfter) / 2.0;
        double avgVSpeed = (status.VSpeed + vspeedAfter) / 2.0;

        VesselStatus result = new VesselStatus();
        result.FuelMass = status.FuelMass - mass;
        result.X = Status.X + duration * avgHSpeed;
        result.Y = Status.Y + duration * avgVSpeed;
        result.HSpeed = hspeedAfter;
        result.VSpeed = vspeedAfter;
        result.State = VesselState.FLIGHT;
        result.Time = status.Time + duration;
        return result;
    }
}
