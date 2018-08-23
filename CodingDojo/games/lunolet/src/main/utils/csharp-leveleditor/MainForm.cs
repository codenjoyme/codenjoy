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
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

namespace LunoletLevelEditor
{
	public partial class MainForm : Form
	{
		private Image _canvas;
		private Graphics _graphics;
		private Relief _relief;
		private Point _cursor;
		private int _width;
		private int _height;
		private int _highest;
		private int _lowest;

		private decimal ScaleRelief => nudScale.Value;

		public MainForm()
		{
			InitializeComponent();
		}

		private void MainForm_Load(object sender, EventArgs e)
		{
			CreateCanvas();
			CreateRelief();
			RefreshView();
		}

		private void CreateRelief(IEnumerable<Point> points = null)
		{
			_relief = new Relief();
			if (points != null && points.Any())
				_relief.Points = new LinkedList<Point>(points);
		}

		private void CreateCanvas()
		{
			_width = Convert.ToInt32(nudWidth.Value);
			_height = Convert.ToInt32(nudHeight.Value);

			_canvas = new Bitmap(_width, _height);
			pbCanvas.Image = _canvas;

			_graphics?.Dispose();
			_graphics = Graphics.FromImage(_canvas);
			_graphics.Clear(Color.White);
		}

		private void pbMap_MouseMove(object sender, MouseEventArgs e)
		{
			lblCoordinates.Text = $"X:{e.X}, Y:{_height-e.Y-1}";
			_cursor = new Point(e.X, e.Y);
			_relief.Move(e.X, e.Y);
			RefreshView();
		}

		private void RefreshView()
		{
			_highest = 0;
			_lowest = _height;
			_graphics.Clear(Color.White);
			_graphics.DrawEllipse(Pens.Black, _cursor.X - 4, _cursor.Y - 4, 7, 7);
			if (_relief.Points.Any())
			{
				foreach (var reliefPoint in _relief.Points)
				{
					_graphics.FillEllipse(Brushes.Black, reliefPoint.X - 4, reliefPoint.Y - 4, 7, 7);
					if (reliefPoint.Y > _highest)
						_highest = reliefPoint.Y;
					if (reliefPoint.Y < _lowest)
						_lowest = reliefPoint.Y;
				}

				if (_relief.Points.Count > 1)
					_graphics.DrawLines(Pens.Black, _relief.Points.ToArray());
			}

			_graphics.DrawLine(Pens.Aquamarine, new Point(0, _lowest+1), new Point(_width - 1, _lowest+1));
			_graphics.DrawLine(Pens.DarkOliveGreen, new Point(0, _highest), new Point(_width - 1, _highest));

			pbCanvas.Refresh();
		}

		private void pbMap_MouseEnter(object sender, EventArgs e)
		{
			_cursor = new Point();
		}

		private void pbMap_MouseDown(object sender, MouseEventArgs e)
		{
			if(e.Button == MouseButtons.Right)
			{
				_relief.RemovePoint(e.X, e.Y);
				RefreshView();
				return;
			}

			if (ModifierKeys.HasFlag(Keys.Control))
			{
				_relief.CaptureAllLocation(e.X, e.Y);
				return;
			}

			if (!_relief.TryCapturePoint(e.X, e.Y))
			{
				_relief.AddPoint(e.X, e.Y);
				RefreshView();
			}
		}

		private void pbMap_MouseUp(object sender, MouseEventArgs e)
		{
			_relief.ReleaseCapturedPoint();
			_relief.ReleaseMovingLocation();
		}

		private void btnViewJson_Click(object sender, EventArgs e)
		{
			var relief = NormalizeRelief(_relief.Points);
			var dryMass = Convert.ToDouble(nudDryMass.Value);
			var targetX = Convert.ToDouble(nudTargetX.Value);
			var x = Convert.ToDouble(nudStatusX.Value);
			var y = Convert.ToDouble(nudStatusY.Value);
			var time = Convert.ToDouble(nudStatusTime.Value);
			var hSpeed = Convert.ToDouble(nudStatusHSpeed.Value);
			var vSpeed = Convert.ToDouble(nudStatusVSpeed.Value);
			var fuelMass = Convert.ToDouble(nudStatusFuelMass.Value);
			var state = Convert.ToInt32(nudStatusState.Value);
			var level = Level.Create(relief, dryMass, targetX, x, y, time, hSpeed, vSpeed, fuelMass, state);
			using (JsonViewForm view = new JsonViewForm(level))
			{
				view.ShowDialog(this);
			}
		}

		private IEnumerable<Point> NormalizeRelief(IEnumerable<Point> reliefPoints)
		{
			var mostLeft = reliefPoints.Select(p => p.X).Min();
			return reliefPoints.Select(p => new Point((int)((p.X - mostLeft) * ScaleRelief), (int)((_highest - p.Y) * ScaleRelief)));
		}

		private void btnLoadJson_Click(object sender, EventArgs e)
		{
			using (JsonViewForm view = new JsonViewForm())
			{
				view.ShowDialog(this);
				var level = view.Level;
				if(level != null)
				{
					LoadMap(level);
					RefreshView();
				}
			}
		}

		private void LoadMap(Level level)
		{
			var relief = level.Relief.Select(rp => new Point((int)((decimal)rp.X / ScaleRelief), (int)((decimal)rp.Y / ScaleRelief)));
			if (relief.Any())
			{
				var sortedByX = relief.Select(p => p.X).OrderBy(x=>x);
				var minX = sortedByX.First();
				var maxX = sortedByX.Last();

				var sortedByY = relief.Select(p => p.Y).OrderBy(y => y);
				var minY = sortedByY.First();
				var maxY = sortedByY.Last();

				nudHeight.Value = (Decimal)(maxY - minY) + 10;
				nudWidth.Value = (Decimal)(maxX - minX) + 10;

				CreateCanvas();
			}

			_relief.Points = new LinkedList<Point>
				(
					relief.Select(rp => new Point(rp.X + 5, _height - rp.Y - 5))
				);

			nudDryMass.Value = (decimal)level.DryMass;
			nudTargetX.Value = (decimal)level.TargetX;
			nudStatusX.Value = (decimal)level.VesselStatus.X;
			nudStatusY.Value = (decimal)level.VesselStatus.Y;
			nudStatusTime.Value = (decimal)level.VesselStatus.Time;
			nudStatusHSpeed.Value = (decimal)level.VesselStatus.HSpeed;
			nudStatusVSpeed.Value = (decimal)level.VesselStatus.VSpeed;
			nudStatusFuelMass.Value = (decimal)level.VesselStatus.FuelMass;
			nudStatusState.Value = level.VesselStatus.State;
		}

		private void btnResizeCanvas_Click(object sender, EventArgs e)
		{
			CreateCanvas();
			RefreshView();
		}

		private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
		{
			_graphics?.Dispose();
		}

		private void btnCreateRelief_Click(object sender, EventArgs e)
		{
			CreateRelief();
			RefreshView();
		}
	}
}
