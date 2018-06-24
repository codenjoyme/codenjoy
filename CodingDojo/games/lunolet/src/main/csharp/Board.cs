using System;

namespace LunoletClient
{
    internal class Board
    {
        public double Time { get; set; }

        public double X { get; set; }

        public double Y { get; set; }

        public double HSpeed { get; set; }

        public double VSpeed { get; set; }

        public double FuelMass { get; set; }

        public string State { get; set; }

        public Point2D[] Relief { get; set; }

        public Point2D[] History { get; set; }

        public double Angle { get; set; }

        public Point2D Target { get; set; }
    }
}
