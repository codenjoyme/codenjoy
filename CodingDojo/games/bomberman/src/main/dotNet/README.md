For C#:
- on page http://<server>/codenjoy-contest/help
    ~ you can read game instructions
        # server = ip:8080 server ip inside your LAN
        # server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://<server>/codenjoy-contest/register
- open Bomberman.sln in VisualStudio
- open file Program.cs from "Demo" project
    = find line and replace value with your email, you entered during registration step
        static string UserName = "user@mail.com";
    = find line and replace 192.168.1.1 to server IP if you play on LAN
        static string Server = "192.168.1.1:8080";
    = or uncomment this line if you want to play on http://codenjoy.com server
        static string Server = "tetrisj.jvmhost.net:12270";
- make "Demo" project as runnable
- now you can run Demo :)
- all "game logic" is in the class "MyCustomBombermanAI" in method "DoMove()"
- Codenjoy!
