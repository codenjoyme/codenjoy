using System.Collections.Generic;
using System.Linq;
using BikeClient.Elements;

namespace BikeClient.GameArea
{
    public abstract class Board : AbstractPlayGround
    {
        public Point GetMe()
        {
            return Get(ElementGroups.MyBike).FirstOrDefault();
        }

        public bool IsGameOver()
        {
            Point me = GetMe();
            return ElementGroups.MyFallenBike.Any(x => IsAt(me, x));
        }

        public bool CheckNearMe(List<QDirection> directions, Element[] elements)
        {
            Point point = GetMe();
            if (point == null)
            {
                return false;
            }

            foreach (QDirection direction in directions)
            {
                point = direction.Change(point);
            }

            return IsAt(point.X, point.Y, elements);
        }

        public bool CheckNearMe(QDirection direction, Element[] elements)
        {
            Point me = GetMe();
            if (me == null)
            {
                return false;
            }

            Point atDirection = direction.Change(me);
            return IsAt(atDirection.X, atDirection.Y, elements);
        }

        public bool CheckAtMe(Element[] elements)
        {
            Point me = GetMe();
            return me != null && IsAt(me, elements);
        }

        public bool IsOutOfFieldRelativeToMe(QDirection direction)
        {
            Point me = GetMe();
            if (me == null)
            {
                return false;
            }

            Point atDirection = direction.Change(me);
            return IsOutOfField(atDirection.X, atDirection.Y);
        }
    }
}