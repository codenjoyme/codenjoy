using System;

namespace LemonadeClient
{
	class Program
	{
		// Server name and port number -- ask orgs
		private static string ServerNameAndPort = "epruryaw0576.moscow.epam.com:43022";
		// Register on the server, write down your registration name
		private static string UserName = "borc1231231236a406kvh";
		// Look up for the code in the browser url after the registration
		private static string UserCode = "1555550509319024989";
		static void Main(string[] args)
		{
			string url = $"ws://{ServerNameAndPort}/codenjoy-contest/ws?user={UserName}&code={UserCode}";
			ClientWrapper client = new ClientWrapper(url, new MyLemonadeBot());

			Console.Read();
		}
	}
}
