using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using SpaceRace.Api;
using SpaceRace.Api.Interfaces;

namespace SpaceRace.UI
{
    public partial class UiForm : Form, IApiLogger
    {

        private const string GameName = "Space Race";
        private Dictionary<Actions, PictureBox> _directionBtns;
        private Dictionary<Actions, Bitmap> _activeBitmaps;
        private Dictionary<Actions, Bitmap> _inactiveBitmaps;
        private Dictionary<Element, Bitmap> _elementBitmaps;
        private int _tileWidth;
        private int _tileHeight;
        private readonly CancellationTokenSource _cts;
        private DateTime? _startTime;
        private DateTime? _endTime;
        private Bitmap _currentCanvas;

        public UiForm(CancellationTokenSource cts)
        {
            _cts = cts;
            InitializeComponent();
            LoadResources();
            GameNameText.Text = GameName;
        }

        delegate void LogCallBack(params object[] messages);
        delegate void LogBoardCallBack(Board board);
        delegate void LogResultCallBack(IDirection direction);

        delegate void ShutDownCallBack();

        public void ShutDown()
        {
            try
            {

                if (InvokeRequired)
                {
                    var invoker = new ShutDownCallBack(ShutDown);
                    Invoke(invoker);
                }
                else
                {
                    Close();
                }
            }
            catch (Exception )
            {
                // does not matter. Just close app, please
            }
        }

        public void Log(params object[] messages)
        {
            if (LogPanel.InvokeRequired)
            {
                var invoker = new LogCallBack(Log);
                Invoke(invoker,new object[] {messages});
            }
            else
            {
                var builder = new StringBuilder();
                foreach (var message in messages)
                {
                    builder.Append(message);
                    builder.Append(", ");
                }
                LogPanel.AppendText(builder.ToString());
                LogPanel.AppendText(Environment.NewLine);
            }
        }
        public void LogBoard(Board board)
        {
            _startTime = DateTime.Now;
            if (FieldPictureBox.InvokeRequired)
            {
                var invoker = new LogBoardCallBack(LogBoard);
                Invoke(invoker, new object[] {board});
            }
            else
            {
                ClearLog();
                CleanDirectionBtns();
                var boardSize = board.Size + 1;

                var tWidth = _tileWidth;
                var tHeight = _tileHeight;
                
                _currentCanvas = new Bitmap(_tileWidth * boardSize, _tileHeight * boardSize);
                using (var grp = Graphics.FromImage(_currentCanvas))
                {
                    foreach (var p in board.GetAllExtend())
                    {
                        var elementBitmap = _elementBitmaps[p.Value];
                        grp.DrawImage(elementBitmap, p.Key.X * tWidth, p.Key.Y * tHeight, tWidth, tHeight);
                    }
                }

                InternalDrawBoard();
            }
        }

        private void InternalDrawBoard()
        {
            if (_currentCanvas == null) return;
            var size = Math.Min(FieldPictureBox.Width, FieldPictureBox.Height);
            
            FieldPictureBox.Image = new Bitmap(size, size);

            using (var drawer = Graphics.FromImage(FieldPictureBox.Image))
            {
                drawer.DrawImage(_currentCanvas, 0, 0, size, size);
            }
        }

    public void LogResult(IDirection direction)
        {
            _endTime = DateTime.Now;
            
            if (actBtn.InvokeRequired)
            {
                var invoker = new LogResultCallBack(LogResult);
                Invoke(invoker, new object[] {direction});
            }
            else
            {
                if (_directionBtns.TryGetValue(direction.Action, out var pictureBox))
                {
                    pictureBox.Image = _activeBitmaps[direction.Action];
                }

                if (direction.IsAct)
                {
                    _directionBtns[Actions.Act].Image = _activeBitmaps[Actions.Act];
                }
                PrintDecisionMakeTime();
            }
        }

        private void PrintDecisionMakeTime()
        {
            if (_startTime != null && _endTime != null)
            {
                var diff = _endTime.Value - _startTime.Value;
                DmtText.Text = $"{diff.Milliseconds} mS";
            }
        }
        
        private void CleanDirectionBtns()
        {
            foreach (var btn in _directionBtns)
            {
                btn.Value.Image = _inactiveBitmaps[btn.Key];
            }
        }

        private void ClearLog()
        {
            LogPanel.Text = "";
        }
        
        private void LoadResources()
        {
            _directionBtns = new Dictionary<Actions, PictureBox>
            {
                { Actions.Left,  this.leftBtn},
                { Actions.Right, this.rightBtn},
                { Actions.Down,  this.downBtn},
                { Actions.Up,    this.upBtn },
                { Actions.Act,   this.actBtn },
            };
            _activeBitmaps = _directionBtns
                .ToDictionary(
                    x => x.Key, 
                    x => LoadActionResource(x.Key, "active"));

            _inactiveBitmaps = _directionBtns
                .ToDictionary(
                    x => x.Key,
                    x => LoadActionResource(x.Key, "inactive"));

            _elementBitmaps = (Enum.GetValues(typeof(Element)) as IEnumerable<Element>)
                .ToDictionary(
                    x => x,
                    LoadElementResource);
            var tile = _elementBitmaps.First().Value;
            _tileHeight = tile.Height;
            _tileWidth = tile.Width;

            CleanDirectionBtns();
        }

        private Bitmap LoadActionResource(Actions action, string postfix)
        {
            return new Bitmap($".\\assets\\{action}_{postfix}.png");
        }

        private Bitmap LoadElementResource(Element element)
        {
            return new Bitmap($".\\assets\\{element}.png");
        }

        private void UiForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            _cts?.Cancel();
        }

        private void UiForm_Resize(object sender, EventArgs e)
        {
            InternalDrawBoard();
        }
    }
}
