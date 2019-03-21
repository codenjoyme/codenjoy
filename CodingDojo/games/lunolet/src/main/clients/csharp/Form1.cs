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
using WebSocketSharp;

namespace LunoletClient
{
    public partial class Form1 : Form
    {
        // Server name and port number -- ask orgs
        private static string ServerNameAndPort = "127.0.0.1:8080";
        // Register on the server, write down your player id (you can get it from baord page url after registration)
        private static string UserId = "3edq63tw0bq4w4iem7nb";
        // Look up for the code in the browser url after the registration
        private static string UserCode = "1889919902398150091";

        private WebSocket client = null;

        private static readonly MyLunoletBot mybot = new MyLunoletBot();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string url = $"ws://{ServerNameAndPort}/codenjoy-contest/ws?user={UserId}&code={UserCode}";
            client = new WebSocket(url);

            client.OnOpen += Client_OnOpen;
            client.OnClose += Client_OnClose;
            client.OnMessage += Client_OnMessage;
            client.OnError += Client_OnError;

            client.Connect();
        }

        private void Form1_FormClosing(Object sender, FormClosingEventArgs e)
        {
            client.Close();
        }

        private void Client_OnError(object sender, ErrorEventArgs args)
        {
            this.Invoke((MethodInvoker)delegate
            {
                LogMessage($"WebSocket Error: {args.Exception}\r\n");
            });
        }

        private void Client_OnMessage(object sender, MessageEventArgs args)
        {
            if (!args.IsText)
                return;

            mybot.Received(args.Data);
            string command = mybot.Process();
            this.Invoke((MethodInvoker) delegate
            {
                LogMessage($"Sending: {command}\r\n");
                pictureBox1.Image = mybot.GetImage();
            });
            client.Send(command);
        }

        private void Client_OnClose(object sender, CloseEventArgs closeEventArgs)
        {
            this.Invoke((MethodInvoker)delegate
            {
                LogMessage("WebSocket Closed\r\n");
            });
        }

        private void Client_OnOpen(object sender, EventArgs eventArgs)
        {
            this.Invoke((MethodInvoker)delegate
            {
                LogMessage("WebSocket Opened\r\n");
            });
        }

        private void LogMessage(string text)
        {
            textBoxLog.Text += text;
            textBoxLog.Select(int.MaxValue, int.MaxValue);
            textBoxLog.ScrollToCaret();
        }
    }
}
