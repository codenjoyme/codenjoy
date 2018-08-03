using System;
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
