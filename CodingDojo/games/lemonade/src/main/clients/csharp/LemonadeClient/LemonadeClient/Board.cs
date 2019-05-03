namespace LemonadeClient
{
	public class Board
	{
		public string Assets { get; set; }

		public string WeatherForecast { get; set; }

		public bool IsBankrupt { get; set; }

		public string Messages { get; set; }

		public int Day { get; set; }

		public double LemonadePrice { get; set; }

		public History[] History { get; set; }

	}
}