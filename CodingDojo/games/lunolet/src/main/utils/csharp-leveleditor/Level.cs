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
﻿using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace LunoletLevelEditor
{
	public class Level
	{
		[JsonProperty("dryMass")]
		public double DryMass { get; set; }

		[JsonProperty("targetX")]
		public double TargetX { get; set; }

		[JsonProperty("relief")]
		public IEnumerable<ReliefPoint> Relief { get; set; }

		[JsonProperty("vesselStatus")]
		public VesselStatusType VesselStatus { get; set; }

		[JsonConstructor]
		private Level(VesselStatusType vs) => this.VesselStatus = vs;

		public static Level Create(IEnumerable<Point> relief, double dryMass, double targetX, double vX, double vY,
			double vTime, double vHSpeed,
			double vVSpeed, double vFuelMass, int vState)
		{
			var vesselStatus = new VesselStatusType
			{
				X = vX,
				Y = vY,
				Time = vTime,
				HSpeed = vHSpeed,
				VSpeed = vVSpeed,
				FuelMass = vFuelMass,
				State = vState
			};

			return new Level(vesselStatus)
			{
				Relief = relief.Select(p=>new ReliefPoint {X=p.X, Y=p.Y}),
				DryMass = dryMass,
				TargetX = targetX
			};
		}
		
		public class VesselStatusType
		{
			[JsonProperty("x")]
			public double X { get; set; }

			[JsonProperty("y")]
			public double Y { get; set; }

			[JsonProperty("time")]
			public double Time { get; set; }

			[JsonProperty("hSpeed")]
			public double HSpeed { get; set; }

			[JsonProperty("vSpeed")]
			public double VSpeed { get; set; }

			[JsonProperty("fuelMass")]
			public double FuelMass { get; set; }

			[JsonProperty("state")]
			public int State { get; set; }
		}

		public class ReliefPoint
		{
			[JsonProperty("x")]
			public double X { get; set; }

			[JsonProperty("y")]
			public double Y { get; set; }
		}
	}
}
