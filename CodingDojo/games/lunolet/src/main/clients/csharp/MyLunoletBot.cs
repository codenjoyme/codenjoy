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
using System.Drawing;
using Newtonsoft.Json;

namespace LunoletClient
{
    internal class MyLunoletBot
    {
        private Board board;

        public string CommandText { get; private set; }

        public void Received(string input)
        {
            if (input.StartsWith("board="))
                input = input.Substring(6);
            board = JsonConvert.DeserializeObject<Board>(input);
        }

        public Image GetImage()
        {
            Bitmap bitmap = new Bitmap(600, 400);
            using (Graphics gfx = Graphics.FromImage(bitmap))
            {
                gfx.FillRectangle(Brushes.White, 0, 0, bitmap.Width, bitmap.Height);
                gfx.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

                Font font = new Font(FontFamily.GenericMonospace, 10f);
                Brush brush = Brushes.Black;

                gfx.DrawString($"TIME  {board.Time}", font, brush, 50, 30);
                gfx.DrawString($"FUEL  {board.FuelMass}", font, brush, 50, 45);
                gfx.DrawString($"STATE {board.State}", font, brush, 50, 60);
                gfx.DrawString($"XPOS {board.X}", font, brush, 200, 30);
                gfx.DrawString($"YPOS {board.Y}", font, brush, 200, 45);
                gfx.DrawString($"HSPEED {board.HSpeed}", font, brush, 350, 30);
                gfx.DrawString($"VSPEED {board.VSpeed}", font, brush, 350, 45);
                if (board.HSpeed >= 0.001)
                {
                    gfx.DrawString("→", font, brush, 500, 30);
                }
                else if (board.HSpeed <= -0.001)
                {
                    gfx.DrawString("←", font, brush, 500, 30);
                }
                if (board.VSpeed >= 0.001)
                {
                    gfx.DrawString("↑", font, brush, 500, 45);
                }
                else if (board.VSpeed <= -0.001)
                {
                    gfx.DrawString("↓", font, brush, 500, 45);
                }

                // scale, move center to (300, 200), and flip vertically
                var scale = 6;
                var xshift = 300 - board.X * scale;
                var yshift = 200 + board.Y * scale;
                gfx.TranslateTransform((float)xshift, (float)yshift);
                gfx.ScaleTransform(scale, -scale);

                // draw relief
                var penRelief = new Pen(Color.DimGray, 1f / scale);
                var relief = board.Relief;
                var reliefLen = relief.Length;
                if (reliefLen > 1)
                {
                    var pt1 = relief[0];
                    for (int i = 1; i < reliefLen; i++)
                    {
                        var pt2 = relief[i];
                        gfx.DrawLine(penRelief, (float)pt1.X, (float)pt1.Y, (float)pt2.X, (float)pt2.Y);
                        pt1 = pt2;
                    }
                }

                // draw history for the last step
                var penHistory = new Pen(Color.Green, 1f / scale);
                var history = board.History;
                var historyLen = history.Length;
                if (historyLen > 1)
                {
                    var pt1 = history[0];
                    for (int i = 1; i < historyLen; i++)
                    {
                        var pt2 = history[i];
                        gfx.DrawLine(penHistory, (float)pt1.X, (float)pt1.Y, (float)pt2.X, (float)pt2.Y);
                        pt1 = pt2;
                    }
                }

                // draw target (same transform)
                var target = board.Target;
                var penTarget = new Pen(Color.Red, 1f / scale);
                if (target != null)
                {
                    gfx.DrawLine(penTarget, (float)(target.X - 8.0 / scale), (float)target.Y, (float)(target.X + 8.0 / scale), (float)target.Y);
                    gfx.DrawLine(penTarget, (float)target.X, (float)(target.Y - 8.0 / scale), (float)target.X, (float)(target.Y + 8.0 / scale));
                }

                // draw the ship
                gfx.ResetTransform();
                gfx.TranslateTransform(300f, 200f);
                gfx.ScaleTransform(scale, -scale);
                gfx.RotateTransform(-(float)board.Angle);
                var penShip = new Pen(Color.Blue, 1f / scale);
                gfx.DrawLine(penShip, 0f, 0f, -1f, -0.2f);
                gfx.DrawLine(penShip, -1f, -0.2f, -0.7f, 1.1f);
                gfx.DrawLine(penShip, -0.7f, 1.1f, 0f, 1.6f);
                gfx.DrawLine(penShip, 0f, 1.6f, 0.7f, 1.1f);
                gfx.DrawLine(penShip, 0.7f, 1.1f, 1f, -0.2f);
                gfx.DrawLine(penShip, 1f, -0.2f, 0f, 0f);

                // draw arrow pointing to target
                if (target != null)
                {
                    var deltaX = target.X - board.X;
                    var deltaY = target.Y - board.Y;
                    var distance = Math.Sqrt(deltaX * deltaX + deltaY * deltaY);
                    if (distance > 1)
                    {
                        var radian = Math.Atan2(deltaY, deltaX); // In radians
                        gfx.ResetTransform();
                        gfx.TranslateTransform(300f, 100f);
                        gfx.RotateTransform(-(float)(radian * 180.0 / Math.PI));
                        gfx.DrawLine(penTarget, -30f, 0f, 0f, -0f);
                        gfx.DrawLine(penTarget, 30f, 0f, 0f, 5f);
                        gfx.DrawLine(penTarget, 0f, 5f, 0f, -5f);
                        gfx.DrawLine(penTarget, 0f, -5f, 30f, 0f);
                    }

                }

                gfx.ResetTransform();
            }

            return bitmap;
        }

        public string Process()
        {
            //Random random = new Random((int)DateTime.Now.Ticks);
            //int move = random.Next(0, 3);

            var target = board.Target;

            if (board.State == "START")
            {
                CommandText = "message('go 0, 0.2, 1')";
            }
            else if (board.Y < target.Y + 4.0 || board.VSpeed < -1.5)
            {
                CommandText = "UP";
            }
            else if (board.X < target.X && board.HSpeed < 3.0)
            {
                CommandText = "RIGHT";
            }
            else if (board.X > target.X && board.HSpeed > -3.0)
            {
                CommandText = "LEFT";
            }
            else
            {
                CommandText = "DOWN";
            }

            return CommandText;
        }
    }
}
