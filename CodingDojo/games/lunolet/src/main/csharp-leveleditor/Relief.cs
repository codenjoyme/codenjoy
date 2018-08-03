using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LunoletLevelEditor
{
	class Relief
	{
		public LinkedList<Point> Points;
		private LinkedListNode<Point> CapturedPoint;
		private Point AllLocation;

		public Relief()
		{
			Points = new LinkedList<Point>();
		}

		public bool TryCapturePoint(int x, int y)
		{
			var currentPosition = new Point(x,y);
			var capturable = Points.Where(p => GetDistance(p, currentPosition) < 10)
				.OrderBy(p => GetDistance(p, currentPosition)).FirstOrDefault();

			if (capturable != default(Point))
			{
				CapturedPoint = Points.Find(capturable);
				return true;
			}

			return false;
		}

		private static double GetDistance(Point a, Point b)
		{
			return Math.Sqrt((a.X - b.X) * (a.X - b.X) + (a.Y - b.Y) * (a.Y - b.Y));
		}

		public void AddPoint(int x, int y)
		{
			Points.AddLast(new Point(x, y));
		}

		internal void RemovePoint(int x, int y)
		{
			var currentPosition = new Point(x, y);
			var toRemove = Points.Where(p => GetDistance(p, currentPosition) < 10)
				   .OrderBy(p => GetDistance(p, currentPosition)).FirstOrDefault();
			if(toRemove != default(Point))
			{
				Points.Remove(toRemove);
			}
		}

		public void Move(int newX, int newY)
		{
			if(AllLocation != Point.Empty)
			{
				var deltaX = AllLocation.X - newX;
				var deltaY = AllLocation.Y - newY;
				Points = new LinkedList<Point>(Points.Select(p => new Point(p.X - deltaX, p.Y - deltaY)));
				AllLocation = new Point(newX, newY);
				return;
			}

			if(CapturedPoint != null)
				CapturedPoint.Value = new Point(newX, newY);
		}

		public void ReleaseCapturedPoint()
		{
			CapturedPoint = null;
		}

		internal void ReleaseMovingLocation()
		{
			AllLocation = Point.Empty;
		}

		internal void CaptureAllLocation(int x, int y)
		{
			AllLocation = new Point(x,y);
		}
	}
}
