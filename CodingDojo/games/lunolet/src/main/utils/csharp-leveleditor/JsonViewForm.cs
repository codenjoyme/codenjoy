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
using System.Windows.Forms;
using Newtonsoft.Json;

namespace LunoletLevelEditor
{
	internal class JsonViewForm: Form
	{
		private RichTextBox rtbJsonLevel;
		private string _level;
		public Level Level { get; private set; }

		public JsonViewForm(Level level):this()
		{
			try
			{
				_level = JsonConvert.SerializeObject(level, Formatting.Indented);
			}
			catch (Exception ex)
			{
				_level = $"Unknown error:\n{ex.Message}";
			}
		}

		public JsonViewForm()
		{
			InitializeComponent();
		}

		private void JsonViewForm_Load(object sender, EventArgs e)
		{
			rtbJsonLevel.Text = _level;
		}

		private void InitializeComponent()
		{
			this.rtbJsonLevel = new System.Windows.Forms.RichTextBox();
			this.SuspendLayout();
			// 
			// rtbJsonLevel
			// 
			this.rtbJsonLevel.Dock = System.Windows.Forms.DockStyle.Fill;
			this.rtbJsonLevel.Location = new System.Drawing.Point(0, 0);
			this.rtbJsonLevel.Name = "rtbJsonLevel";
			this.rtbJsonLevel.Size = new System.Drawing.Size(330, 467);
			this.rtbJsonLevel.TabIndex = 0;
			this.rtbJsonLevel.Text = "";
			// 
			// JsonViewForm
			// 
			this.ClientSize = new System.Drawing.Size(330, 467);
			this.Controls.Add(this.rtbJsonLevel);
			this.Name = "JsonViewForm";
			this.Text = "JSON representation";
			this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.JsonViewForm_FormClosing);
			this.Load += new System.EventHandler(this.JsonViewForm_Load);
			this.ResumeLayout(false);

		}

		private void JsonViewForm_FormClosing(object sender, FormClosingEventArgs e)
		{
			Level = null;
			if (!string.IsNullOrWhiteSpace(rtbJsonLevel.Text))
			{
				try
				{
					Level = JsonConvert.DeserializeObject<Level>(rtbJsonLevel.Text);
				}
				catch(Exception ex)
				{
					// ignored
				}
			}
		}
	}
}
