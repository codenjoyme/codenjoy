To create a new configuration for running the game in the IDE, you need to do
the following.

![ide-run-configurations.png](ide-run-configurations.png)

1. Open the run settings window (Run -> Edit Configurations...).
2. Select the Maven section.
3. Clone the `games/sample [install]` configuration (1) (2)
naming the game accordingly (3). Don't forget to change the root directory of the project
inside the settings.
4. Do the same with the `server [run sample]` configuration - it is used to run
the server with one of these games with a pre-build of all dependent artifacts:
client/java, games.pom, engine. Here it is worth paying attention to the change of the game build (5)
and the `-Pgame` parameter that runs the maven profile with this game (6). If there is a need
to run the server with several games, then you should specify all of them in (5), and in (6)
write the names separated by commas: `-Pgame1,game2`.
5. The same should be done with the `server [run-with sample]` configuration -
it is used to run the server with one game, but without pre-building all
dependencies, which speeds up the process.

These custom configurations should not be committed to the repository.