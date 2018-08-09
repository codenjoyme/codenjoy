Codenjoy framework Maven Repository
-----------

This is Maven repository. To connect components to your project, type the following in your pom.xml file.
```
<repositories>
    <repository>
        <id>codenjoy-releases</id>
        <url>https://github.com/codenjoyme/codenjoy-repo/raw/master/snapshots</url>
    </repository>
</repositories>
```
And one of several dependencies
- for the interface part (games are based on these interfaces)
```
<dependency>
    <groupId>com.codenjoy</groupId>
    <artifactId>engine</artifactId>
    <version>${codenjoy.version}</version>
</dependency>
```
- for a specific game 
```
<dependency>
    <groupId>com.codenjoy</groupId>
    <artifactId>${game.name}-engine</artifactId>
    <version>${codenjoy.version}</version>
</dependency>
```
- for the codenjoy server
```
<dependency>
    <groupId>com.codenjoy</groupId>
    <artifactId>codenjoy-contest</artifactId>
    <version>${codenjoy.version}</version>
</dependency>
```
- for the game assembler (assembles a server with selected games, [click here for details](https://github.com/codenjoyme/codenjoy-builder)
```
<dependency>
    <groupId>com.codenjoy</groupId>
    <artifactId>codenjoy-builder</artifactId>
    <version>${codenjoy.version}</version>
</dependency>
```
- the latest version as of now
```
<properties>
    <codenjoy.version>1.0.25</codenjoy.version>
</properties>
```
- a choice of games
```
<properties>
    <game.name>sample-engine</game.name>
    <!--
        <game.name>a2048-engine</game.name>
        <game.name>a2048-extreme-engine</game.name>
        <game.name>bomberman-engine</game.name>
        <game.name>battlecity-engine</game.name>
        <game.name>chess-engine</game.name>
        <game.name>collapse-engine</game.name>
        <game.name>hex-engine</game.name>
        <game.name>loderunner-engine</game.name>
        <game.name>minesweeper-engine</game.name>
        <game.name>rubicscube-engine</game.name>
        <game.name>sudoku-engine</game.name>
        <game.name>snake-engine</game.name>
        <game.name>moebius-engine</game.name>
        <game.name>football-engine</game.name>
        <game.name>startandjump-engine</game.name>
        <game.name>quake2d-engine</game.name>
        <game.name>pong-engine</game.name>
        <game.name>fifteen-engine</game.name>
        <game.name>puzzlebox-engine</game.name>
        <game.name>spacerace-engine</game.name>
        <game.name>reversi-engine</game.name>
    -->
</properties>
```

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========