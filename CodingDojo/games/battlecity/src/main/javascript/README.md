Registration:
- on page http://server/codenjoy-contest/help
    + you can read game instructions
        * server = server_host_ip:8080 server ip inside your LAN
        * server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://server/codenjoy-contest/register
- copy board page browser url from address bar and paste into 'url' variable of 'solver.js' 
- select your game in line 'Games.init('battlecity');'
- write your own bot at 'get' function

For JavaScript with browser:
- write bot
- run 'run-client.html'
- for testing run 'run-test.html'

For JavaScript with node.js:
- install Node.js from http://nodejs.org/
- update Path System variable - add node.js root folder
- write bot
- run 'run-client.bat' or 'npm start' command
- another way to change server url - run 'run-client.bat "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789"'
- for testing run 'run-test.bat' or 'npm test' command