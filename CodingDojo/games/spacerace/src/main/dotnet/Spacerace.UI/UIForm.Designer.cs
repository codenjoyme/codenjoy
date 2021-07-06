
namespace SpaceRace.UI
{
    partial class UiForm
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
            this.Header = new System.Windows.Forms.Panel();
            this.DmtLabel = new System.Windows.Forms.Label();
            this.DmtText = new System.Windows.Forms.Label();
            this.GameNameText = new System.Windows.Forms.Label();
            this.GameNameLabel = new System.Windows.Forms.Label();
            this.Footer = new System.Windows.Forms.Panel();
            this.LogPanel = new System.Windows.Forms.TextBox();
            this.actBtn = new System.Windows.Forms.PictureBox();
            this.rightBtn = new System.Windows.Forms.PictureBox();
            this.upBtn = new System.Windows.Forms.PictureBox();
            this.downBtn = new System.Windows.Forms.PictureBox();
            this.leftBtn = new System.Windows.Forms.PictureBox();
            this.FieldPanel = new System.Windows.Forms.Panel();
            this.FieldPictureBox = new System.Windows.Forms.PictureBox();
            this.Header.SuspendLayout();
            this.Footer.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.actBtn)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.rightBtn)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.upBtn)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.downBtn)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.leftBtn)).BeginInit();
            this.FieldPanel.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.FieldPictureBox)).BeginInit();
            this.SuspendLayout();
            // 
            // Header
            // 
            this.Header.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.Header.Controls.Add(this.DmtLabel);
            this.Header.Controls.Add(this.DmtText);
            this.Header.Controls.Add(this.GameNameText);
            this.Header.Controls.Add(this.GameNameLabel);
            this.Header.Location = new System.Drawing.Point(12, 12);
            this.Header.Name = "Header";
            this.Header.Size = new System.Drawing.Size(422, 53);
            this.Header.TabIndex = 0;
            // 
            // DmtLabel
            // 
            this.DmtLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.DmtLabel.AutoSize = true;
            this.DmtLabel.Location = new System.Drawing.Point(104, 16);
            this.DmtLabel.Name = "DmtLabel";
            this.DmtLabel.Size = new System.Drawing.Size(147, 20);
            this.DmtLabel.TabIndex = 3;
            this.DmtLabel.Text = "Decision make time: ";
            // 
            // DmtText
            // 
            this.DmtText.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.DmtText.AutoSize = true;
            this.DmtText.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.DmtText.Location = new System.Drawing.Point(250, 16);
            this.DmtText.Name = "DmtText";
            this.DmtText.Size = new System.Drawing.Size(99, 20);
            this.DmtText.TabIndex = 2;
            this.DmtText.Text = "0000000000";
            // 
            // GameNameText
            // 
            this.GameNameText.AutoSize = true;
            this.GameNameText.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.GameNameText.Location = new System.Drawing.Point(68, 16);
            this.GameNameText.Name = "GameNameText";
            this.GameNameText.Size = new System.Drawing.Size(0, 20);
            this.GameNameText.TabIndex = 1;
            // 
            // GameNameLabel
            // 
            this.GameNameLabel.AutoSize = true;
            this.GameNameLabel.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.GameNameLabel.Location = new System.Drawing.Point(4, 16);
            this.GameNameLabel.Name = "GameNameLabel";
            this.GameNameLabel.Size = new System.Drawing.Size(55, 20);
            this.GameNameLabel.TabIndex = 0;
            this.GameNameLabel.Text = "Game: ";
            // 
            // Footer
            // 
            this.Footer.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.Footer.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.Footer.Controls.Add(this.LogPanel);
            this.Footer.Controls.Add(this.actBtn);
            this.Footer.Controls.Add(this.rightBtn);
            this.Footer.Controls.Add(this.upBtn);
            this.Footer.Controls.Add(this.downBtn);
            this.Footer.Controls.Add(this.leftBtn);
            this.Footer.Location = new System.Drawing.Point(12, 418);
            this.Footer.Name = "Footer";
            this.Footer.Size = new System.Drawing.Size(422, 142);
            this.Footer.TabIndex = 1;
            // 
            // LogPanel
            // 
            this.LogPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.LogPanel.Enabled = false;
            this.LogPanel.Location = new System.Drawing.Point(89, 8);
            this.LogPanel.Multiline = true;
            this.LogPanel.Name = "LogPanel";
            this.LogPanel.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.LogPanel.Size = new System.Drawing.Size(326, 123);
            this.LogPanel.TabIndex = 8;
            // 
            // actBtn
            // 
            this.actBtn.Location = new System.Drawing.Point(29, 28);
            this.actBtn.Name = "actBtn";
            this.actBtn.Size = new System.Drawing.Size(24, 24);
            this.actBtn.TabIndex = 7;
            this.actBtn.TabStop = false;
            // 
            // rightBtn
            // 
            this.rightBtn.Location = new System.Drawing.Point(59, 29);
            this.rightBtn.Name = "rightBtn";
            this.rightBtn.Size = new System.Drawing.Size(24, 24);
            this.rightBtn.TabIndex = 6;
            this.rightBtn.TabStop = false;
            // 
            // upBtn
            // 
            this.upBtn.Location = new System.Drawing.Point(29, -1);
            this.upBtn.Name = "upBtn";
            this.upBtn.Size = new System.Drawing.Size(24, 24);
            this.upBtn.TabIndex = 5;
            this.upBtn.TabStop = false;
            // 
            // downBtn
            // 
            this.downBtn.Location = new System.Drawing.Point(29, 58);
            this.downBtn.Name = "downBtn";
            this.downBtn.Size = new System.Drawing.Size(24, 24);
            this.downBtn.TabIndex = 4;
            this.downBtn.TabStop = false;
            // 
            // leftBtn
            // 
            this.leftBtn.Location = new System.Drawing.Point(-1, 28);
            this.leftBtn.Name = "leftBtn";
            this.leftBtn.Size = new System.Drawing.Size(24, 24);
            this.leftBtn.TabIndex = 3;
            this.leftBtn.TabStop = false;
            // 
            // FieldPanel
            // 
            this.FieldPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.FieldPanel.Controls.Add(this.FieldPictureBox);
            this.FieldPanel.Location = new System.Drawing.Point(12, 71);
            this.FieldPanel.Name = "FieldPanel";
            this.FieldPanel.Size = new System.Drawing.Size(422, 341);
            this.FieldPanel.TabIndex = 2;
            // 
            // FieldPictureBox
            // 
            this.FieldPictureBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.FieldPictureBox.Location = new System.Drawing.Point(0, 0);
            this.FieldPictureBox.Name = "FieldPictureBox";
            this.FieldPictureBox.Size = new System.Drawing.Size(422, 329);
            this.FieldPictureBox.TabIndex = 0;
            this.FieldPictureBox.TabStop = false;
            // 
            // UiForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(446, 572);
            this.Controls.Add(this.FieldPanel);
            this.Controls.Add(this.Footer);
            this.Controls.Add(this.Header);
            this.Name = "UiForm";
            this.Text = "UIForm";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.UiForm_FormClosing);
            this.Resize += new System.EventHandler(this.UiForm_Resize);
            this.Header.ResumeLayout(false);
            this.Header.PerformLayout();
            this.Footer.ResumeLayout(false);
            this.Footer.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.actBtn)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.rightBtn)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.upBtn)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.downBtn)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.leftBtn)).EndInit();
            this.FieldPanel.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.FieldPictureBox)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel Header;
        private System.Windows.Forms.Panel Footer;
        private System.Windows.Forms.Panel FieldPanel;
        private System.Windows.Forms.PictureBox FieldPictureBox;
        private System.Windows.Forms.PictureBox actBtn;
        private System.Windows.Forms.PictureBox rightBtn;
        private System.Windows.Forms.PictureBox upBtn;
        private System.Windows.Forms.PictureBox downBtn;
        private System.Windows.Forms.PictureBox leftBtn;
        private System.Windows.Forms.TextBox LogPanel;
        private System.Windows.Forms.Label GameNameText;
        private System.Windows.Forms.Label GameNameLabel;
        private System.Windows.Forms.Label DmtLabel;
        private System.Windows.Forms.Label DmtText;
    }
}