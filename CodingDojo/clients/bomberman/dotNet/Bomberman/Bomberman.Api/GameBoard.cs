using System;
using System.Collections.Generic;
using System.Linq;

namespace Bomberman.Api
{
    public class GameBoard
    {
        public GameBoard(String boardString)
        {
            BoardString = boardString.Replace("\n", "");
        }

        public String BoardString { get; private set; }

        /// <summary>
        /// GameBoard size (actual board size is Size x Size cells)
        /// </summary>
        public int Size
        {
            get
            {
                return (int)Math.Sqrt(BoardString.Length);
            }
        }

        public BoardPoint MyBombermanPosition
        {
            get
            {
                return FindAllElements(BoardElement.Bomberman)
                    .Concat(FindAllElements(BoardElement.BombBomberman))
                    .Concat(FindAllElements(BoardElement.DeadBomberman))
                    .Single();
            }
        }

        public bool MyBombermanDead
        {
            get
            {
                return BoardString.Contains((char)BoardElement.DeadBomberman);
            }
        }

        public List<BoardPoint> GetOtherBombermanPositions()
        {
            return FindAllElements(BoardElement.OtherBomberman)
                .Concat(FindAllElements(BoardElement.OtherBombBomberman))
                .Concat(FindAllElements(BoardElement.OtherDeadBomberman))
                .ToList();
        }

        public bool HasElementAt(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
            {
                return false;
            }

            return GetElementAt(point) == element;
        }

        public BoardElement GetElementAt(BoardPoint point)
        {
            return (BoardElement)BoardString[GetShiftByPoint(point)];
        }

        /// <summary>
        /// Writes board view to the console window
        /// </summary>
        public void PrintBoard()
        {
            for (int i = 0; i < Size; i++)
            {
                Console.WriteLine(BoardString.Substring(i * Size, Size));
            }
        }

        public List<BoardPoint> GetBarrierPositions()
        {
            return GetMeatChopperPositions()
                .Concat(GetWallPositions())
                .Concat(GetBombPositions())
                .Concat(GetWallDestroyablePositions())
                .Concat(GetOtherBombermanPositions())
                .Distinct()
                .ToList();
        }

        public List<BoardPoint> GetMeatChopperPositions()
        {
            return FindAllElements(BoardElement.MeatChopper);
        }

        public List<BoardPoint> FindAllElements(BoardElement element)
        {
            List<BoardPoint> result = new List<BoardPoint>();

            for (int i = 0; i < Size * Size; i++)
            {
                BoardPoint pt = GetPointByShift(i);

                if (HasElementAt(pt, element))
                {
                    result.Add(pt);
                }
            }

            return result;
        }

        public List<BoardPoint> GetWallPositions()
        {
            return FindAllElements(BoardElement.Wall);
        }

        public List<BoardPoint> GetWallDestroyablePositions()
        {
            return FindAllElements(BoardElement.WallDestroyable);
        }

        public List<BoardPoint> GetBombPositions()
        {
            return FindAllElements(BoardElement.BombTimer1)
                .Concat(FindAllElements(BoardElement.BombTimer2))
                .Concat(FindAllElements(BoardElement.BombTimer3))
                .Concat(FindAllElements(BoardElement.BombTimer4))
                .Concat(FindAllElements(BoardElement.BombTimer5))
                .Concat(FindAllElements(BoardElement.BombBomberman))
                .ToList();
        }

        public List<BoardPoint> GetBlastPositions()
        {
            return FindAllElements(BoardElement.Boom);
        }

        public List<BoardPoint> GetFutureBlastPositions()
        {
            var bombs = GetBombPositions()
                .Concat(FindAllElements(BoardElement.OtherBombBomberman))
                .Concat(FindAllElements(BoardElement.BombBomberman));

            var result = new List<BoardPoint>();

            foreach (var bomb in bombs)
            {
                result.Add(bomb);
                result.Add(bomb.ShiftLeft());
                result.Add(bomb.ShiftRight());
                result.Add(bomb.ShiftTop());
                result.Add(bomb.ShiftBottom());
            }

            return result.Where(blast => !blast.IsOutOfBoard(Size) && !GetWallPositions().Contains(blast)).Distinct().ToList();
        }

        public bool HasElementAt(BoardPoint point, params BoardElement[] elements)
        {
            return elements.Any(elem => HasElementAt(point, elem));
        }

        public bool IsNearToElement(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
                return false;

            return HasElementAt(point.ShiftBottom(), element)
                   || HasElementAt(point.ShiftTop(), element)
                   || HasElementAt(point.ShiftLeft(), element)
                   || HasElementAt(point.ShiftRight(), element);
        }

        public bool HasBarrierAt(BoardPoint point)
        {
            return GetBarrierPositions().Contains(point);
        }

        public int GetCountElementsNearToPoint(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
                return 0;

            //GetHashCode() in classic MS.NET for bool returns 1 for true and 0 for false;
            return HasElementAt(point.ShiftLeft(), element).GetHashCode() +
                   HasElementAt(point.ShiftRight(), element).GetHashCode() +
                   HasElementAt(point.ShiftTop(), element).GetHashCode() +
                   HasElementAt(point.ShiftBottom(), element).GetHashCode();
        }

        private int GetShiftByPoint(BoardPoint point)
        {
            return point.Y * Size + point.X;
        }

        private BoardPoint GetPointByShift(int shift)
        {
            return new BoardPoint(shift % Size, shift / Size);
        }
    }
}