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
using System;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using SpaceRace.Api;
using SpaceRace.Api.Interfaces;
using SpaceRace.Player;
using SpaceRace.UI;

namespace SpaceRace
{
    class Program
    {
        private static void ShowForm(object obj)
        {
            var form = obj as UiForm;
            if (obj != null)
            {
                Application.Run(form);
            }
        }

        static void Main(string[] args)
        {
            Application.SetHighDpiMode(HighDpiMode.SystemAware);
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            
            // creating and starting a bot instance
            Console.OutputEncoding = Encoding.Unicode;

            var cts = new CancellationTokenSource();
            UiForm form = Configuration.IsUiEnabled
                ? new UiForm(cts)
                : null;
            var logger = Configuration.IsUiEnabled 
                ? new CompositeLogger(form, new Logger()) as IApiLogger
                : new Logger() as IApiLogger;

           
            if (form != null)
            {
                var uiThread = new Thread(ShowForm);
                uiThread.Start(form);
            }

            var bot = new Solver(logger);
            using var api = new Api.Api(
                Configuration.ConnectionString, 
                Configuration.ReconnectionIntervalMS, 
                bot,
                logger,
                cts);

            // waiting for any key
            if (Configuration.IsUiEnabled)
            {
                cts.Token.WaitHandle.WaitOne();
            }
            else
            {
                Console.ReadKey();
                // on any key - asking AI client to stop.
            }

            api.Stop();
            form?.ShutDown();
        }
    }
}
