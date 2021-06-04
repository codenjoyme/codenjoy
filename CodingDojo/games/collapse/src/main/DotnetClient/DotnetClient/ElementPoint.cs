namespace CollapseClient
{
	public class ElementPoint: Point
	{
		public readonly Element Element;

		public ElementPoint(int x, int y, Element e): base(x,y)
		{
			Element = e;
		}

		/// <summary>
		/// Returns new BoardPoint object shifted left to "delta" points
		/// </summary>
		public override Point ShiftLeft(int delta = 1)
		{
			return new ElementPoint(X - delta, Y, Element);
		}

		/// <summary>
		/// Returns new BoardPoint object shifted right to "delta" points
		/// </summary>
		public override Point ShiftRight(int delta = 1)
		{
			return new ElementPoint(X + delta, Y, Element);
		}

		/// <summary>
		/// Returns new BoardPoint object shifted top "delta" points
		/// </summary>
		public override Point ShiftTop(int delta = 1)
		{
			return new ElementPoint(X, Y + delta, Element);
		}

		/// <summary>
		/// Returns new BoardPoint object shifted bottom "delta" points
		/// </summary>
		public override Point ShiftBottom(int delta = 1)
		{
			return new ElementPoint(X, Y - delta, Element);
		}

		public static bool operator ==(ElementPoint p1, ElementPoint p2)
		{
			if (ReferenceEquals(p1, p2))
				return true;

			if (ReferenceEquals(p1, null) || ReferenceEquals(p2, null))
				return false;

			return p1.X == p2.X && p1.Y == p2.Y && p1.Element == p2.Element;
		}

		public static bool operator !=(ElementPoint p1, ElementPoint p2)
		{
			return !(p1 == p2);
		}

		public override string ToString()
		{
			return string.Format("[{0},{1},{2}]", X, Y, Element);
		}

		public override bool Equals(object obj)
		{
			if (obj == null) return false;
			if (!(obj is ElementPoint)) return false;

			ElementPoint that = (ElementPoint)obj;

			return that.X == this.X && that.Y == this.Y && that.Element == this.Element;
		}

		public override int GetHashCode()
		{
			int hash = 23;
			hash = hash * 31 + X;
			hash = hash * 31 + Y;
			hash = hash * 31 + (int)Element;
			return hash;
		}
	}
}
