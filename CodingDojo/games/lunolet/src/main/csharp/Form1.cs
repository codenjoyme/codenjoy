using System;
using System.Windows.Forms;
using WebSocketSharp;

namespace LunoletClient
{
    public partial class Form1 : Form
    {
        // Server name and port number -- ask orgs
        private static string ServerNameAndPort = "127.0.0.1:8080";
        // Register on the server, write down your registration name
        private static string UserName = "your@email.com";
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
            string url = $"ws://{ServerNameAndPort}/codenjoy-contest/ws?user={UserName}&code={UserCode}";
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
