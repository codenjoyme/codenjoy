For C#:
- on page http://<server>/codenjoy-contest/help
    ~ you can read game instructions
        # server = ip:8080 server ip inside your LAN
        # server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://<server>/codenjoy-contest/register
- open Bomberman.sln in VisualStudio
- open file Program.cs from "Demo" project
    = find line
	    var bomber = new MyCustomBombermanAI("user@gmail.com");
    = replace 'user@gmail.com' with your email, you entered during registration step
- open file BombermanBase.cs
    = find line
        protected readonly string Server =
            @"ws://192.168.1.1:8080/codenjoy-contest/ws";
    = replace 192.168.1.1 to server IP if you play on LAN
    = uncomment line if you want to play on http://codenjoy.com server
        protected readonly string Server =
            @"ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws";
- now you can run Demo :)
- all "game logic" is in the class "MyCustomBombermanAI" in method "DoMove()"
- Codenjoy!

