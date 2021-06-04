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
using SpaceRace.Api.Interfaces;

namespace SpaceRace.Api
{
    public class Direction : IDirection
    {
        public static IDirection UP    = new Direction(Actions.Up, 0, -1);
        public static IDirection DOWN  = new Direction(Actions.Down, 0, 1);
        public static IDirection LEFT  = new Direction(Actions.Left, -1, 0);
        public static IDirection RIGHT = new Direction(Actions.Right, 1, 0);
        public static IDirection STOP  = new Direction(Actions.Stop, 0, 0);
        public static IDirection ACT   = new Direction(Actions.Act, 0, 0, true);
        public static IDirection SUICIDE = new Direction(Actions.Suicide, 0, 0, true);

        private readonly Actions _action;
        private readonly int _dx;
        private readonly int _dy;

        public bool IsAct { get; }

        public Direction(Actions action, int dx = 0, int dy = 0, bool isAct = false)
        {
            _action = action;
            _dx = dx;
            _dy = dy;
            IsAct = isAct;
        }
        
        public IDirection WithAct()
        {
            if (IsAct) return this;
            return new Direction(_action, _dx, _dy, true);
        }

        public Point Change(Point point)
        {
            return new Point(point.X + _dx, point.Y + _dy);
        }

        public override bool Equals(object obj)
        {
            var their = obj as Direction;
            if (their == null) return false;
            return IsAct == their.IsAct && their._action == _action;
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return IsAct ? 1000 : 0 + (int)_action;
            }
        }
        
        public override string ToString()
        {
            if (_action == Actions.Act) return Actions.Act.ToString();
            if (_action == Actions.Suicide) return Actions.Act + "(0)";
            return _action + (IsAct ? "," + Actions.Act : "");
        }
    }
}