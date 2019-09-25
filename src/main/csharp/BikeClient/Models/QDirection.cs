/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
namespace BikeClient
{
    public class QDirection
    {
        public int dx;
        public int dy;
        public int value;
        private string directionString;

        QDirection(int value, int dx, int dy, string directionName) {
            this.value = value;
            this.dx = dx;
            this.dy = dy;
            this.directionString = directionName;
        }

        public static QDirection Stop()
        {
            return new QDirection(1, 0, 0, "");
        }

        public static QDirection  Up()
        {
            return new QDirection(2,0,-1, "UP");
        }

        public static QDirection Down()
        {
            return new QDirection(3,0,1, "DOWN");
        }

        public int ChangeX(int x)
        {
            return dx + x;
        }
        public int ChangeY(int y)
        {
            return dy + y;
        }

        public Point Change(Point point)
        {
            return Point.pt(point.X+dx,point.Y+dy);
        }

        public override string ToString()
        {
            return this.directionString;
        }
    }
}
