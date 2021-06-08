using System;
using SnakeBattle.Enums;

namespace SnakeBattle.Models
{
    public class Cell : IEquatable<Cell>
    {
        /// <summary>
        ///     Model represents one cell on board.
        /// </summary>
        /// <param name="type">Type of cell</param>
        /// <param name="coordinateX">Horizontal position (column)</param>
        /// <param name="coordinateY">Vertical position (row)</param>
        public Cell(CellType type, int coordinateX, int coordinateY)
        {
            Type = type;
            CoordinateX = coordinateX;
            CoordinateY = coordinateY;
        }

        public CellType Type { get; }
        public int CoordinateX { get; }
        public int CoordinateY { get; }

        public bool Equals(Cell other)
        {
            if (ReferenceEquals(null, other))
            {
                return false;
            }

            if (ReferenceEquals(this, other))
            {
                return true;
            }

            return Type == other.Type && CoordinateX == other.CoordinateX && CoordinateY == other.CoordinateY;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj))
            {
                return false;
            }

            if (ReferenceEquals(this, obj))
            {
                return true;
            }

            if (obj.GetType() != GetType())
            {
                return false;
            }

            return Equals((Cell) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                var hashCode = (int) Type;
                hashCode = (hashCode * 397) ^ CoordinateX;
                hashCode = (hashCode * 397) ^ CoordinateY;
                return hashCode;
            }
        }

        public static bool operator ==(Cell a, Cell b)
        {
            return
                a?.Type == b?.Type
                && a?.CoordinateX == b?.CoordinateX
                && a?.CoordinateY == b?.CoordinateY;
        }

        public static bool operator !=(Cell a, Cell b)
        {
            return !(a == b);
        }

        public override string ToString()
        {
            return $"x: {CoordinateX}, y: {CoordinateY}, {Type.ToString()}";
        }
    }
}