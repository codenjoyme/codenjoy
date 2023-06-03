In addition to the full-fledged server, it is also possible to run a lightweight
server with a single game. In this case, the codenjoy server in
the classic sense will not be launched - only the WS server for
serving connected WS clients will be launched.

This option is good for those who want to maximize the use of
machine learning capabilities. There are several advantages:
1. You can reduce the timeout time for 1 tick to 1ms, which will reduce the learning time -
   more iterations in less time.
2. You can configure the WS server so that it waits for the response of the WS client before
   the next game tick is calculated (in the original version, no one is waiting for anyone -
   if you managed to answer the move in 1000 ms, you did it, you did not have time - you skip the move).
3. You can specify the responses of how many exactly WS clients should be expected before calculating
   the next game tick.
4. Before starting the WS server, you can flexibly set the game settings
   (the same ones that the Sensei sets on the admin panel - which the player usually does not have access to).
5. You can configure detailed logging, which will allow you to more closely examine the game.
6. You can directly influence the pseudo-random inside the game engine, which
   will allow you to train the algorithm on a strictly deterministic machine.

To run the server, you will need Java.

For Windows:
- Download Microsoft JDK ([for Windows here](https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip))
- Unpack the contents into the `C:\Java\jdk-11` folder so that the `bin` folder and others are inside.
- Create an environment variable `JAVA_HOME` equal to `C:\Java\jdk-11`.
- Add to the end of the environment variable `Path` the line `;%JAVA_HOME%\bin`

For Linux:
- TBD

To independently generate a jar with such a lightweight WS server, it is enough
run in the sources [\CodingDojo\build\build-local-ws-game-server-jar.sh](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/build/build-local-ws-game-server-jar.sh),
specify the name of the game and wait for the result in the `.\out` folder. After assembly there
will be several files:
- `<GAMENAME>-engine.jar` - the server itself.
- `run-local-server.sh` - script to run the server.
- `.env` - server configuration file.

To change the settings of the WS server, the `.env` configuration file is used:
```GAME=mollymage
HOST=127.0.0.1
PORT=8080
TIMEOUT=1000
LOG_DISABLE=false
LOG_FILE=output.txt
LOG_TIME=true
SHOW_PLAYERS=1,2
RANDOM_SEED=random-soul-string
WAIT_FOR=2
SETTINGS={'POTIONS_COUNT':11,'POTION_POWER':7}
```

Here:
- `GAME` points to the name of the game.
- `HOST` and `PORT` are WS server settings.
- `TIMEOUT` delay time in ms after all clients.
  (specified in `WAIT_FOR`) responded.
- `LOG_DISABLE` true if logging to a file is enabled.
- `LOG_FILE` log file name.
- `LOG_TIME` true - if you need to output the time to the log file.
- `SHOW_PLAYERS` indicates for which users to output information to the log
  (the ordinal number is determined by the sequence of connecting the WS client).
- `RANDOM_SEED` salt for pseudo-random in the game engine. If you specify the same
  line, the game (and all random objects) will behave the same.
- `WAIT_FOR` how many WS clients to wait for before
  delay for `TIMEOUT` with subsequent calculation of the next game tick.
- `SETTINGS` json with game settings (the same ones that the Sensei sets on the admin panel).

Please note that the server is stopped by the 
Ctrl-C key in the console of the `run-local-server.sh` script. If you close the console window -
the server will remain "hanging" in memory and to complete it you will need to find the java 
process in the Task Manager.