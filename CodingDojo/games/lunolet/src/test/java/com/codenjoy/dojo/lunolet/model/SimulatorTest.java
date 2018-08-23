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


import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class SimulatorTest {

    @Test
    public void takeOff_smallThrust_failedToTakeOff() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        sut.simulate(0, 0.1, 1);
        assertVesselStatus(sut.Status, 0.0, 0.0, 0.0, 0.0, 50.0, 0.0, VesselState.START);
        assertEquals(1, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOff1_1second() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        sut.simulate(0, 0.2, 1);
        assertVesselStatus(sut.Status, 0.82, 0.41, 0.0, 0.0, 49.8, 1.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOff2_5seconds() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        sut.simulate(0, 1, 5);
        assertVesselStatus(sut.Status, 4.116, 10.274, 0.0, 0.0, 49.0, 5.0, VesselState.FLIGHT);
        assertEquals(6, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void freeFall1_1second() {
        Simulator sut = new Simulator();
        sut.Status.Y = 1.0;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -1.62, 0.19, 0.0, 0.0, 50.0, 1.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_1second() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 0.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing2_VerticalFreeFallFrom1m_2seconds() {
        Simulator sut = new Simulator();
        sut.Status.Y = 1.0;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(0, 0, 2);
        assertVesselStatus(sut.Status, -1.8, 0.0, 0.0, 0.0, 50.0, 1.111, VesselState.LANDED);
        assertEquals(3, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing3_VerticalThrustedFrom20cm_1second() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(0, 0.1, 1);
        assertVesselStatus(sut.Status, -0.4, 0.0, 0.0, 0.0, 49.9, 1.0, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing4_VerticalThrustedFrom10cm_1second() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.1;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(0, 0.1, 1);
        assertVesselStatus(sut.Status, -0.283, 0.0, 0.0, 0.0, 49.929, 0.707, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_LandElevated10m() {
        Simulator sut = new Simulator();
        sut.Status.Y = 10.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, 10));
        sut.Relief.add(new Point2D.Double(50, 10));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 10.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalThrustedFrom20cm_LandElevated10m() {
        Simulator sut = new Simulator();
        sut.Status.Y = 10.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, 10));
        sut.Relief.add(new Point2D.Double(50, 10));

        sut.simulate(0, 0.1, 1);
        assertVesselStatus(sut.Status, -0.4, 10.0, 0.0, 0.0, 49.9, 1.0, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_LandElevatedMinus10m() {
        Simulator sut = new Simulator();
        sut.Status.Y = -9.8;
        sut.Status.State = VesselState.FLIGHT;

        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, -10));
        sut.Relief.add(new Point2D.Double(50, -10));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, -10.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_LandSloped() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;
        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, -50));
        sut.Relief.add(new Point2D.Double(50, 50));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 0.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.reset();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;
        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, 50));
        sut.Relief.add(new Point2D.Double(50, -50));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 0.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_LandOnMountainTop() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, -50));
        sut.Relief.add(new Point2D.Double(0, 0));
        sut.Relief.add(new Point2D.Double(50, -50));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 0.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void landing_VerticalFreeFallFrom20cm_LandInCanyonBottom() {
        Simulator sut = new Simulator();
        sut.Status.Y = 0.2;
        sut.Status.State = VesselState.FLIGHT;

        sut.Relief.clear();
        sut.Relief.add(new Point2D.Double(-50, 50));
        sut.Relief.add(new Point2D.Double(0, 0));
        sut.Relief.add(new Point2D.Double(50, 50));

        sut.simulate(0, 0, 1);
        assertVesselStatus(sut.Status, -0.805, 0.0, 0.0, 0.0, 50.0, 0.497, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void flight_10degreeThrust_1second() {
        Simulator sut = new Simulator();
        sut.Status.Y = 1.0;
        sut.Status.State = VesselState.FLIGHT;

        sut.simulate(10, 0.1, 1);
        assertVesselStatus(sut.Status, -0.419, 0.791, 0.212, 0.106, 49.9, 1.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(10.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void flight_askingMoreFuelThanFuelMass() {
        Simulator sut = new Simulator();
        sut.Status.Y = 1.0;
        sut.Status.State = VesselState.FLIGHT;
        sut.Status.FuelMass = 0.1;

        sut.simulate(0, 0.2, 1);
        assertVesselStatus(sut.Status, 0.653, 1.163, 0.0, 0.0, 0.0, 0.5, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOffAndLanding1_HeavyVessel_VerticalFlight() {
        Simulator sut = new Simulator();
        sut.DryMass = 2150;
        sut.Status.FuelMass = 400;
        sut.Status.State = VesselState.START;

        sut.simulate(0, 20, 10);
        assertVesselStatus(sut.Status, 12.608, 62.851, 0.0, 0.0, 380, 10.0, VesselState.FLIGHT);
        assertEquals(11, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 0, 7.7);
        assertVesselStatus(sut.Status, 0.134, 111.906, 0.0, 0.0, 380, 17.7, VesselState.FLIGHT);
        assertEquals(9, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 0, 8);
        assertVesselStatus(sut.Status, -12.826, 61.135, 0.0, 0.0, 380, 25.7, VesselState.FLIGHT);
        assertEquals(9, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 17, 8);
        assertVesselStatus(sut.Status, -1.121, 5.238, 0.0, 0.0, 363, 33.7, VesselState.FLIGHT);
        assertEquals(9, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 1.5, 1);
        assertVesselStatus(sut.Status, -0.556, 4.399, 0.0, 0.0, 361.5, 34.7, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 5, 5);
        assertVesselStatus(sut.Status, -1.315, 0.0, 0.0, 0.0, 356.803, 39.397, VesselState.LANDED);
        assertEquals(6, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOffAndLanding2_FlightFor100m() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        sut.simulate(0, 0.2, 1);
        assertVesselStatus(sut.Status, 0.82, 0.41, 0.0, 0.0, 49.8, 1.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 0.8, 4);
        assertVesselStatus(sut.Status, 4.116, 10.274, 0.0, 0.0, 49.0, 5.0, VesselState.FLIGHT);
        assertEquals(5, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 1.0, 10);
        assertVesselStatus(sut.Status, 0.176, 31.7, 0.0, 0.0, 48.0, 15.0, VesselState.FLIGHT);
        assertEquals(11, sut.History.size());

        sut.simulate(10, 2.0, 15);
        assertVesselStatus(sut.Status, 0.142, 33.88, 4.279, 32.056, 46.0, 30.0, VesselState.FLIGHT);
        assertEquals(16, sut.History.size());
        assertEquals(10.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 1.0, 8);
        assertVesselStatus(sut.Status, -0.435, 32.683, 4.279, 66.287, 45.0, 38.0, VesselState.FLIGHT);
        assertEquals(9, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(-10, 2.0, 16);
        assertVesselStatus(sut.Status, -1.84, 14.265, -0.044, 100.207, 43.0, 54.0, VesselState.FLIGHT);
        assertEquals(17, sut.History.size());
        assertEquals(-10.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 1.1, 8);
        assertVesselStatus(sut.Status, -1.037, 2.724, -0.044, 99.857, 41.9, 62.0, VesselState.FLIGHT);
        assertEquals(9, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 0.55, 4);
        assertVesselStatus(sut.Status, -0.71, 0, -0.044, 99.721, 41.471, 65.117, VesselState.LANDED);
        assertEquals(5, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOffAndLanding3_UpUpDownDown() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        sut.simulate(0, 0.2, 1);
        assertVesselStatus(sut.Status, 0.82, 0.41, 0.0, 0.0, 49.8, 1.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(0, 0.2, 1);
        assertVesselStatus(sut.Status, 1.642, 1.641, 0.0, 0.0, 49.6, 2.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(0.0, sut.LastAngle, 1e-5);

        sut.simulate(180, 0.1, 1);
        assertVesselStatus(sut.Status, -1.2, 1.862, 0.0, 0.0, 49.5, 3.0, VesselState.FLIGHT);
        assertEquals(2, sut.History.size());
        assertEquals(180.0, sut.LastAngle, 1e-5);

        sut.simulate(180, 0.1, 1);
        assertVesselStatus(sut.Status, -3.467, 0.0, 0.0, 0.0, 49.42, 3.798, VesselState.LANDED);
        assertEquals(2, sut.History.size());
        assertEquals(180.0, sut.LastAngle, 1e-5);
    }

    @Test
    public void takeOff_TooMuchAcceleration_PilotUnconscious_FreeFlight() {
        Simulator sut = new Simulator();
        sut.Status.State = VesselState.START;

        double angle = 33.825683069334218;
        sut.simulate(angle, 4.5, 0.25);

        assertVesselStatus(sut.Status, -45.404, 0, 30.561, 1713.104, 45.5, 56.18, VesselState.CRASHED);
        assertEquals(58, sut.History.size());
        assertEquals(angle, sut.LastAngle, 1e-5);
    }

    private static void assertVesselStatus(
            VesselStatus status,
            double vspeed, double y, double hspeed, double x, double fuelmass, double time, VesselState state) {

        assertEquals(vspeed, Math.round(status.VSpeed * 1000.0) / 1000.0, 1e-5);
        assertEquals(y, Math.round(status.Y * 1000.0) / 1000.0, 1e-5);
        assertEquals(hspeed, Math.round(status.HSpeed * 1000.0) / 1000.0, 1e-5);
        assertEquals(x, Math.round(status.X * 1000.0) / 1000.0, 1e-5);
        assertEquals(fuelmass, Math.round(status.FuelMass * 1000.0) / 1000.0, 1e-5);
        assertEquals(time, Math.round(status.Time * 1000.0) / 1000.0, 1e-5);
        assertEquals(state, status.State);
    }

}
