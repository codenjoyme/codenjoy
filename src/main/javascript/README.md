Registration:
- on page http://server/codenjoy-contest/help
    + you can read game instructions
        * server = server_host_ip:8080 server ip inside your LAN
        * server = codenjoy.com if you play on http://codenjoy.com/codenjoy-contest
- register your hero on server http://server/codenjoy-contest/register
- copy board page browser url from address bar and paste into url variable of Runner.js
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

Написание вашего бота (JavaScript)
·Для создания бота, вам потребуется реализовать ровно один метод в классе Runner.js -> DirectionSolver -> get(){};
·Ответом должна быть одна из двух команд: ”UP” или “DOWN” или ничего;
·Для вашего удобства класс Board уже содержит ряд утильных методов для парсинга состояния поля;
·Помимо классов DirectionSolver и Board, вам также предоставлены Elements(различные состояния вашего и вражеских мотоциклов, все возможные элементы на поле, кроме элементов трамплинов, элементы, являющиеся составными частями трамплинов), содержащие все возможные элементы, встречающиеся в игре;
·Используя эти элементы и методы Board вы сможете выяснить что содержит объект board в нужном вам месте, и выстроить логику бота в зависимости от этого.