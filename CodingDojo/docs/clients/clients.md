Clients we call projects that allow the player in an event
using WebSocket to connect to the server and play on behalf of their hero.
It is in the clients that the artificial intelligence code is written.

Since we use WebSocket technology - this allows us to get
advantages:
- You can play in any programming language, as long as the
  WebSocket library for it has been released.
- You can play on your local machine (your favorite ide) without uploading the code
  anywhere on the server - just run the process locally and play,
  turn off the process - the hero stands still.
- you can debug in production environment - every second the server
  requests a client action command for the hero by sending it a test
  the board representation (fields) and here you have an opportunity to stop the 
  debugger code client and check its state. All this time the bot will stand motionless
  and the game on the server of course will not stop.

To make life as easy as possible for the client we develop new and support
existing clients for popular programming languages. Each client must:
- support all existing games
- allow to run on a clean system (without preinstalled programs)
  - from *.bat files for Windows
  - *.sh files for Linux/MacOS
  - from a Dockerfile for Docker connoisseurs.
    That's why these scripts are able to:
- download the tools you need from the Internet
- install them in a local folder (or use a preinstalled one)
- and then compile
- and run the client
- as well as tests for it