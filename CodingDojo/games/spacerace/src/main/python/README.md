SpaceRace
=========

websocket client app for Codenjoy SpaceRace

Designed to be run with python3 (tested on 3.7.9 version)

To connect to the game server:
1. Prepare envieronment. Run .\_perepare.bat to create Python virtual envieronment, activate it, install necessary packages and run unit tests suite to ensure that client ready. (.\_clean.bat will deactivate virtual envieronment)
2. Sign up. If you did everything right, you'll get to the main game board.
3. Click on your name on the right hand side panel
4. Copy the whole link from the browser, go to configuration.py and paste it to the connectionString(self) method, now you're good to go!

The bot actions logic should be implemented in solver.py file. Solver.get() method should return the action for your bot to perform. 
You can use generic Board API methods in your Solver.
The instance of Solver is created due client initialization. Internal data fields could be shared between Solver.get method calls. 
Solver.get(board) get a Board class object as parameter and should retur Direction or Command classes instance. 

Wse the main.py to start you client.