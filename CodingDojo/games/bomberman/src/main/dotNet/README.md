For C#:
- on page {serverHost}/codenjoy-contest/help, or now for bomberman link is: https://dojorena.io/codenjoy-contest/resources/help/bomberman.html
    + you can read game instructions
        * serverHost = server_host_ip:8080 server ip inside your LAN
		* serverHost = dojorena.io if you play on https://dojorena.io/codenjoy-contest
        * serverHost = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server {serverHost}/codenjoy-contest/register
- after registering you'll be redirected to the game and will recieve URL needed for connecting client
- open Bomberman.sln in VisualStudio
- open file Program.cs from "Demo" project
    + find line and replace 'ServerUrl' value with your URL address, you recieved  after sign in to the game
        * static string ServerUrl = "{serverHost}/codenjoy-contest/board/player/1a2b3c4d5e6f7g?code=1234567891011&gameName=bomberman";
- make "Demo" project as runnable
- now you can run Demo :)
- all "game logic" is in the class "YourSolver" in method 
	+ string Get(Board board)
- Codenjoy!
