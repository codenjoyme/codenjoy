Codenjoy framework Maven Repository
-----------

Тут находится Maven репозиторий. Для подключения компонентов к своему проекту укажи в своем `pom.xml`
```
<repositories>
	<repository>
		<id>codenjoy-releases</id>
		<url>https://github.com/codenjoyme/codenjoy-repo/raw/master/snapshots</url>
	</repository>
</repositories>
```
А так же одну из нескольких депенденсей
- для интерфейсной части (на основе тих интерфейсов создаются игры)
```
<dependency>
	<groupId>com.codenjoy</groupId>
	<artifactId>sample-engine</artifactId>
	<version>${codenjoy.version}</version>
</dependency>
```
- для той или иной игры 
```
<dependency>
	<groupId>com.codenjoy</groupId>
	<artifactId>${game.name}-engine</artifactId>
	<version>${codenjoy.version}</version>
</dependency>
```
- для сервера codenjoy
```
<dependency>
	<groupId>com.codenjoy</groupId>
    <artifactId>codenjoy-contest</artifactId>
	<version>${codenjoy.version}</version>
</dependency>
```
- для сборщика игор (собирает сервер совместно с выбранными играми, [детальнее тут|https://github.com/codenjoyme/codenjoy-builder])
```
<dependency>
	<groupId>com.codenjoy</groupId>
    <artifactId>codenjoy-builder</artifactId>
	<version>${codenjoy.version}</version>
</dependency>
```
- последняя на данный момент версия
```
<properties>
	<codenjoy.version>1.0.7</codenjoy.version>
</properties>
```
- варианты игор
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
	-->
</properties>
```

Другие материалы
--------------
Больше [деталей тут](https://github.com/codenjoyme/codenjoy)

[Команда Codenjoy](http://codenjoy.com/portal/?page_id=51)
===========