bomberman
=========

websocket client app for Codenjoy Bomberman

Designed to be run with python3
Depends on websocket-client from https://github.com/liris/websocket-client/blob/py3/websocket.py

To connect to the game server:
1. Sign up. If you did everything right, you'll get to the main game board.
2. Click on your name on the right hand side panel
3. Copy the whole link from the browser, go to main.py and paste it to the main() method, now you're good to go!

The bomberman actions logic should be implemented in dds.py file. DirectionSolver get() method should return the action
for your bomberman to perform. You can use generic Board API methods in your DirectionSolver. 
