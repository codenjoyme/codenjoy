Registration:
- on page http://server/codenjoy-contest/help
    + you can read game instructions
        * server = server_host_ip:8080 server ip inside your LAN
        * server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://server/codenjoy-contest/register
- change variable at Runner.js
    + var hostIp = server_host_ip;
        * to use 'tetrisj.jvmhost.net' for play on http://codenjoy.com/codenjoy-contest
        * to use server_host_ip for play on LAN
- change your email at Runner.js
    + var userName = 'user@gmail.com';
- write your own bot at 'get' function

For JavaScript with browser:
- write bot
- run run-client.html

For JavaScript with node.js:
- install Node.js from http://nodejs.org/
- update Path System variable - add node.js root folder
- setup new node.js library - run
    + npm install ws
- write bot
- run run-client.bat