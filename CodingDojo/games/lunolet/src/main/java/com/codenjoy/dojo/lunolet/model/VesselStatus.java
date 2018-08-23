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


import java.awt.geom.Point2D;

public class VesselStatus {

    public double Time;

    public double X;

    public double Y;

    public double HSpeed;

    public double VSpeed;

    public double FuelMass;

    public VesselState State;

    public double Consumption;

    public boolean isNotFinalState() {
        return State != VesselState.CRASHED && State != VesselState.LANDED;
    }

    public Point2D.Double getPoint() {
        return new Point2D.Double(X, Y);
    }

    public double getSpeed() {
        return Math.sqrt(HSpeed * HSpeed + VSpeed * VSpeed);
    }
}
