using System;
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