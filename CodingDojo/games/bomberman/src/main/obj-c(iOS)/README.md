For ObjectiveC:
- install xCode if needed
- open Bombermen.xcodeproj
- on page http://<server>/codenjoy-contest/help
    ~ you can read game instructions
        # server = ip:8080 server ip inside your LAN
        # server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://<server>/codenjoy-contest/register
- change server URL if needed in BombermanAPI.m
    = codenjoy.com:80 for play on http://codenjoy.com
    = <server_ip>:8080 for play on LAN (server_ip for example = 192.168.1.1)
- start following server with function and your email
    [[BombermanAPI sharedApi] newGameWithUserName:@"your@gmail.com"];


