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
﻿namespace LunoletLevelEditor
{
	partial class MainForm
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.groupBox1 = new System.Windows.Forms.GroupBox();
			this.btnResizeCanvas = new System.Windows.Forms.Button();
			this.groupBox2 = new System.Windows.Forms.GroupBox();
			this.label11 = new System.Windows.Forms.Label();
			this.nudStatusState = new System.Windows.Forms.NumericUpDown();
			this.label10 = new System.Windows.Forms.Label();
			this.nudStatusVSpeed = new System.Windows.Forms.NumericUpDown();
			this.label9 = new System.Windows.Forms.Label();
			this.nudStatusHSpeed = new System.Windows.Forms.NumericUpDown();
			this.label8 = new System.Windows.Forms.Label();
			this.nudStatusTime = new System.Windows.Forms.NumericUpDown();
			this.label7 = new System.Windows.Forms.Label();
			this.nudStatusY = new System.Windows.Forms.NumericUpDown();
			this.label6 = new System.Windows.Forms.Label();
			this.nudStatusFuelMass = new System.Windows.Forms.NumericUpDown();
			this.label5 = new System.Windows.Forms.Label();
			this.nudStatusX = new System.Windows.Forms.NumericUpDown();
			this.nudTargetX = new System.Windows.Forms.NumericUpDown();
			this.label4 = new System.Windows.Forms.Label();
			this.nudDryMass = new System.Windows.Forms.NumericUpDown();
			this.label3 = new System.Windows.Forms.Label();
			this.btnCreateRelief = new System.Windows.Forms.Button();
			this.nudHeight = new System.Windows.Forms.NumericUpDown();
			this.label2 = new System.Windows.Forms.Label();
			this.nudWidth = new System.Windows.Forms.NumericUpDown();
			this.label1 = new System.Windows.Forms.Label();
			this.btnViewJson = new System.Windows.Forms.Button();
			this.pbCanvas = new System.Windows.Forms.PictureBox();
			this.panel1 = new System.Windows.Forms.Panel();
			this.lblCoordinates = new System.Windows.Forms.Label();
			this.nudScale = new System.Windows.Forms.NumericUpDown();
			this.label12 = new System.Windows.Forms.Label();
			this.btnLoadJson = new System.Windows.Forms.Button();
			this.groupBox1.SuspendLayout();
			this.groupBox2.SuspendLayout();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusState)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusVSpeed)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusHSpeed)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusTime)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusY)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusFuelMass)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusX)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudTargetX)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudDryMass)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudHeight)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.nudWidth)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.pbCanvas)).BeginInit();
			this.panel1.SuspendLayout();
			((System.ComponentModel.ISupportInitialize)(this.nudScale)).BeginInit();
			this.SuspendLayout();
			// 
			// groupBox1
			// 
			this.groupBox1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.groupBox1.Controls.Add(this.btnResizeCanvas);
			this.groupBox1.Controls.Add(this.groupBox2);
			this.groupBox1.Controls.Add(this.nudTargetX);
			this.groupBox1.Controls.Add(this.label4);
			this.groupBox1.Controls.Add(this.nudDryMass);
			this.groupBox1.Controls.Add(this.label3);
			this.groupBox1.Controls.Add(this.btnCreateRelief);
			this.groupBox1.Controls.Add(this.nudHeight);
			this.groupBox1.Controls.Add(this.label2);
			this.groupBox1.Controls.Add(this.nudWidth);
			this.groupBox1.Controls.Add(this.label1);
			this.groupBox1.Location = new System.Drawing.Point(567, 12);
			this.groupBox1.Name = "groupBox1";
			this.groupBox1.Size = new System.Drawing.Size(221, 364);
			this.groupBox1.TabIndex = 0;
			this.groupBox1.TabStop = false;
			this.groupBox1.Text = "Level settings";
			// 
			// btnResizeCanvas
			// 
			this.btnResizeCanvas.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.btnResizeCanvas.Location = new System.Drawing.Point(124, 71);
			this.btnResizeCanvas.Name = "btnResizeCanvas";
			this.btnResizeCanvas.Size = new System.Drawing.Size(91, 23);
			this.btnResizeCanvas.TabIndex = 10;
			this.btnResizeCanvas.Text = "Resize canvas";
			this.btnResizeCanvas.UseVisualStyleBackColor = true;
			this.btnResizeCanvas.Click += new System.EventHandler(this.btnResizeCanvas_Click);
			// 
			// groupBox2
			// 
			this.groupBox2.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.groupBox2.Controls.Add(this.label11);
			this.groupBox2.Controls.Add(this.nudStatusState);
			this.groupBox2.Controls.Add(this.label10);
			this.groupBox2.Controls.Add(this.nudStatusVSpeed);
			this.groupBox2.Controls.Add(this.label9);
			this.groupBox2.Controls.Add(this.nudStatusHSpeed);
			this.groupBox2.Controls.Add(this.label8);
			this.groupBox2.Controls.Add(this.nudStatusTime);
			this.groupBox2.Controls.Add(this.label7);
			this.groupBox2.Controls.Add(this.nudStatusY);
			this.groupBox2.Controls.Add(this.label6);
			this.groupBox2.Controls.Add(this.nudStatusFuelMass);
			this.groupBox2.Controls.Add(this.label5);
			this.groupBox2.Controls.Add(this.nudStatusX);
			this.groupBox2.Location = new System.Drawing.Point(6, 155);
			this.groupBox2.Name = "groupBox2";
			this.groupBox2.Size = new System.Drawing.Size(209, 204);
			this.groupBox2.TabIndex = 9;
			this.groupBox2.TabStop = false;
			this.groupBox2.Text = "Vessel status";
			// 
			// label11
			// 
			this.label11.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label11.AutoSize = true;
			this.label11.Location = new System.Drawing.Point(12, 177);
			this.label11.Name = "label11";
			this.label11.Size = new System.Drawing.Size(32, 13);
			this.label11.TabIndex = 21;
			this.label11.Text = "State";
			// 
			// nudStatusState
			// 
			this.nudStatusState.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusState.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusState.Location = new System.Drawing.Point(89, 175);
			this.nudStatusState.Maximum = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusState.Name = "nudStatusState";
			this.nudStatusState.Size = new System.Drawing.Size(114, 20);
			this.nudStatusState.TabIndex = 22;
			// 
			// label10
			// 
			this.label10.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label10.AutoSize = true;
			this.label10.Location = new System.Drawing.Point(12, 125);
			this.label10.Name = "label10";
			this.label10.Size = new System.Drawing.Size(48, 13);
			this.label10.TabIndex = 19;
			this.label10.Text = "V Speed";
			// 
			// nudStatusVSpeed
			// 
			this.nudStatusVSpeed.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusVSpeed.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusVSpeed.Location = new System.Drawing.Point(89, 123);
			this.nudStatusVSpeed.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusVSpeed.Name = "nudStatusVSpeed";
			this.nudStatusVSpeed.Size = new System.Drawing.Size(114, 20);
			this.nudStatusVSpeed.TabIndex = 20;
			// 
			// label9
			// 
			this.label9.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label9.AutoSize = true;
			this.label9.Location = new System.Drawing.Point(12, 99);
			this.label9.Name = "label9";
			this.label9.Size = new System.Drawing.Size(49, 13);
			this.label9.TabIndex = 17;
			this.label9.Text = "H Speed";
			// 
			// nudStatusHSpeed
			// 
			this.nudStatusHSpeed.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusHSpeed.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusHSpeed.Location = new System.Drawing.Point(89, 97);
			this.nudStatusHSpeed.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusHSpeed.Name = "nudStatusHSpeed";
			this.nudStatusHSpeed.Size = new System.Drawing.Size(114, 20);
			this.nudStatusHSpeed.TabIndex = 18;
			// 
			// label8
			// 
			this.label8.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label8.AutoSize = true;
			this.label8.Location = new System.Drawing.Point(12, 73);
			this.label8.Name = "label8";
			this.label8.Size = new System.Drawing.Size(30, 13);
			this.label8.TabIndex = 15;
			this.label8.Text = "Time";
			// 
			// nudStatusTime
			// 
			this.nudStatusTime.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusTime.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusTime.Location = new System.Drawing.Point(89, 71);
			this.nudStatusTime.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusTime.Name = "nudStatusTime";
			this.nudStatusTime.Size = new System.Drawing.Size(114, 20);
			this.nudStatusTime.TabIndex = 16;
			// 
			// label7
			// 
			this.label7.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label7.AutoSize = true;
			this.label7.Location = new System.Drawing.Point(12, 47);
			this.label7.Name = "label7";
			this.label7.Size = new System.Drawing.Size(14, 13);
			this.label7.TabIndex = 13;
			this.label7.Text = "Y";
			// 
			// nudStatusY
			// 
			this.nudStatusY.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusY.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusY.Location = new System.Drawing.Point(89, 45);
			this.nudStatusY.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusY.Name = "nudStatusY";
			this.nudStatusY.Size = new System.Drawing.Size(114, 20);
			this.nudStatusY.TabIndex = 14;
			// 
			// label6
			// 
			this.label6.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label6.AutoSize = true;
			this.label6.Location = new System.Drawing.Point(12, 151);
			this.label6.Name = "label6";
			this.label6.Size = new System.Drawing.Size(54, 13);
			this.label6.TabIndex = 11;
			this.label6.Text = "Fuel mass";
			// 
			// nudStatusFuelMass
			// 
			this.nudStatusFuelMass.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusFuelMass.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusFuelMass.Location = new System.Drawing.Point(89, 149);
			this.nudStatusFuelMass.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusFuelMass.Name = "nudStatusFuelMass";
			this.nudStatusFuelMass.Size = new System.Drawing.Size(114, 20);
			this.nudStatusFuelMass.TabIndex = 12;
			this.nudStatusFuelMass.Value = new decimal(new int[] {
            50,
            0,
            0,
            0});
			// 
			// label5
			// 
			this.label5.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label5.AutoSize = true;
			this.label5.Location = new System.Drawing.Point(12, 21);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(14, 13);
			this.label5.TabIndex = 10;
			this.label5.Text = "X";
			// 
			// nudStatusX
			// 
			this.nudStatusX.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudStatusX.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudStatusX.Location = new System.Drawing.Point(89, 19);
			this.nudStatusX.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudStatusX.Name = "nudStatusX";
			this.nudStatusX.Size = new System.Drawing.Size(114, 20);
			this.nudStatusX.TabIndex = 10;
			// 
			// nudTargetX
			// 
			this.nudTargetX.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudTargetX.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudTargetX.Location = new System.Drawing.Point(95, 129);
			this.nudTargetX.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudTargetX.Name = "nudTargetX";
			this.nudTargetX.Size = new System.Drawing.Size(120, 20);
			this.nudTargetX.TabIndex = 8;
			// 
			// label4
			// 
			this.label4.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label4.AutoSize = true;
			this.label4.Location = new System.Drawing.Point(12, 131);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(65, 13);
			this.label4.TabIndex = 7;
			this.label4.Text = "Target X, px";
			// 
			// nudDryMass
			// 
			this.nudDryMass.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudDryMass.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudDryMass.Location = new System.Drawing.Point(95, 103);
			this.nudDryMass.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudDryMass.Name = "nudDryMass";
			this.nudDryMass.Size = new System.Drawing.Size(120, 20);
			this.nudDryMass.TabIndex = 6;
			this.nudDryMass.Value = new decimal(new int[] {
            250,
            0,
            0,
            0});
			// 
			// label3
			// 
			this.label3.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(12, 105);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(50, 13);
			this.label3.TabIndex = 5;
			this.label3.Text = "Dry mass";
			// 
			// btnCreateRelief
			// 
			this.btnCreateRelief.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.btnCreateRelief.Location = new System.Drawing.Point(26, 71);
			this.btnCreateRelief.Name = "btnCreateRelief";
			this.btnCreateRelief.Size = new System.Drawing.Size(92, 23);
			this.btnCreateRelief.TabIndex = 4;
			this.btnCreateRelief.Text = "Create relief";
			this.btnCreateRelief.UseVisualStyleBackColor = true;
			this.btnCreateRelief.Click += new System.EventHandler(this.btnCreateRelief_Click);
			// 
			// nudHeight
			// 
			this.nudHeight.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudHeight.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudHeight.Location = new System.Drawing.Point(95, 45);
			this.nudHeight.Maximum = new decimal(new int[] {
            100000,
            0,
            0,
            0});
			this.nudHeight.Name = "nudHeight";
			this.nudHeight.Size = new System.Drawing.Size(120, 20);
			this.nudHeight.TabIndex = 3;
			this.nudHeight.Value = new decimal(new int[] {
            480,
            0,
            0,
            0});
			// 
			// label2
			// 
			this.label2.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(12, 47);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(77, 13);
			this.label2.TabIndex = 2;
			this.label2.Text = "Map height, px";
			// 
			// nudWidth
			// 
			this.nudWidth.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudWidth.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudWidth.Location = new System.Drawing.Point(95, 19);
			this.nudWidth.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudWidth.Name = "nudWidth";
			this.nudWidth.Size = new System.Drawing.Size(120, 20);
			this.nudWidth.TabIndex = 1;
			this.nudWidth.Value = new decimal(new int[] {
            640,
            0,
            0,
            0});
			// 
			// label1
			// 
			this.label1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(12, 21);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(73, 13);
			this.label1.TabIndex = 0;
			this.label1.Text = "Map width, px";
			// 
			// btnViewJson
			// 
			this.btnViewJson.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.btnViewJson.Location = new System.Drawing.Point(716, 382);
			this.btnViewJson.Name = "btnViewJson";
			this.btnViewJson.Size = new System.Drawing.Size(72, 23);
			this.btnViewJson.TabIndex = 1;
			this.btnViewJson.Text = "View JSON";
			this.btnViewJson.UseVisualStyleBackColor = true;
			this.btnViewJson.Click += new System.EventHandler(this.btnViewJson_Click);
			// 
			// pbCanvas
			// 
			this.pbCanvas.Cursor = System.Windows.Forms.Cursors.Cross;
			this.pbCanvas.Location = new System.Drawing.Point(3, 3);
			this.pbCanvas.Name = "pbCanvas";
			this.pbCanvas.Size = new System.Drawing.Size(113, 94);
			this.pbCanvas.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
			this.pbCanvas.TabIndex = 2;
			this.pbCanvas.TabStop = false;
			this.pbCanvas.MouseDown += new System.Windows.Forms.MouseEventHandler(this.pbMap_MouseDown);
			this.pbCanvas.MouseEnter += new System.EventHandler(this.pbMap_MouseEnter);
			this.pbCanvas.MouseMove += new System.Windows.Forms.MouseEventHandler(this.pbMap_MouseMove);
			this.pbCanvas.MouseUp += new System.Windows.Forms.MouseEventHandler(this.pbMap_MouseUp);
			// 
			// panel1
			// 
			this.panel1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.panel1.AutoScroll = true;
			this.panel1.BackColor = System.Drawing.SystemColors.ActiveCaption;
			this.panel1.Controls.Add(this.pbCanvas);
			this.panel1.Location = new System.Drawing.Point(12, 12);
			this.panel1.Name = "panel1";
			this.panel1.Size = new System.Drawing.Size(549, 426);
			this.panel1.TabIndex = 3;
			// 
			// lblCoordinates
			// 
			this.lblCoordinates.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.lblCoordinates.AutoSize = true;
			this.lblCoordinates.BackColor = System.Drawing.Color.White;
			this.lblCoordinates.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
			this.lblCoordinates.Location = new System.Drawing.Point(567, 425);
			this.lblCoordinates.Name = "lblCoordinates";
			this.lblCoordinates.Size = new System.Drawing.Size(74, 13);
			this.lblCoordinates.TabIndex = 3;
			this.lblCoordinates.Text = "Coordinates";
			// 
			// nudScale
			// 
			this.nudScale.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.nudScale.DecimalPlaces = 3;
			this.nudScale.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
			this.nudScale.Location = new System.Drawing.Point(608, 382);
			this.nudScale.Maximum = new decimal(new int[] {
            10000000,
            0,
            0,
            0});
			this.nudScale.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            196608});
			this.nudScale.Name = "nudScale";
			this.nudScale.Size = new System.Drawing.Size(63, 20);
			this.nudScale.TabIndex = 8;
			this.nudScale.Value = new decimal(new int[] {
            1,
            0,
            0,
            65536});
			// 
			// label12
			// 
			this.label12.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.label12.AutoSize = true;
			this.label12.Location = new System.Drawing.Point(568, 384);
			this.label12.Name = "label12";
			this.label12.Size = new System.Drawing.Size(34, 13);
			this.label12.TabIndex = 7;
			this.label12.Text = "Scale";
			// 
			// btnLoadJson
			// 
			this.btnLoadJson.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.btnLoadJson.Location = new System.Drawing.Point(716, 411);
			this.btnLoadJson.Name = "btnLoadJson";
			this.btnLoadJson.Size = new System.Drawing.Size(72, 23);
			this.btnLoadJson.TabIndex = 9;
			this.btnLoadJson.Text = "Load JSON";
			this.btnLoadJson.UseVisualStyleBackColor = true;
			this.btnLoadJson.Click += new System.EventHandler(this.btnLoadJson_Click);
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(800, 450);
			this.Controls.Add(this.lblCoordinates);
			this.Controls.Add(this.btnLoadJson);
			this.Controls.Add(this.nudScale);
			this.Controls.Add(this.label12);
			this.Controls.Add(this.btnViewJson);
			this.Controls.Add(this.groupBox1);
			this.Controls.Add(this.panel1);
			this.Name = "MainForm";
			this.Text = "Level editor | Lunolet";
			this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
			this.Load += new System.EventHandler(this.MainForm_Load);
			this.groupBox1.ResumeLayout(false);
			this.groupBox1.PerformLayout();
			this.groupBox2.ResumeLayout(false);
			this.groupBox2.PerformLayout();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusState)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusVSpeed)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusHSpeed)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusTime)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusY)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusFuelMass)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudStatusX)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudTargetX)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudDryMass)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudHeight)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.nudWidth)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.pbCanvas)).EndInit();
			this.panel1.ResumeLayout(false);
			this.panel1.PerformLayout();
			((System.ComponentModel.ISupportInitialize)(this.nudScale)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.Button btnCreateRelief;
		private System.Windows.Forms.NumericUpDown nudHeight;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.NumericUpDown nudWidth;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Button btnViewJson;
		private System.Windows.Forms.PictureBox pbCanvas;
		private System.Windows.Forms.Panel panel1;
		private System.Windows.Forms.GroupBox groupBox2;
		private System.Windows.Forms.Label label11;
		private System.Windows.Forms.NumericUpDown nudStatusState;
		private System.Windows.Forms.Label label10;
		private System.Windows.Forms.NumericUpDown nudStatusVSpeed;
		private System.Windows.Forms.Label label9;
		private System.Windows.Forms.NumericUpDown nudStatusHSpeed;
		private System.Windows.Forms.Label label8;
		private System.Windows.Forms.NumericUpDown nudStatusTime;
		private System.Windows.Forms.Label label7;
		private System.Windows.Forms.NumericUpDown nudStatusY;
		private System.Windows.Forms.Label label6;
		private System.Windows.Forms.NumericUpDown nudStatusFuelMass;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.NumericUpDown nudStatusX;
		private System.Windows.Forms.NumericUpDown nudTargetX;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.NumericUpDown nudDryMass;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label lblCoordinates;
		private System.Windows.Forms.NumericUpDown nudScale;
		private System.Windows.Forms.Label label12;
		private System.Windows.Forms.Button btnLoadJson;
		private System.Windows.Forms.Button btnResizeCanvas;
	}
}

