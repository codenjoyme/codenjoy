# Ruby client for Bomberman game

## How to setup Codenjoy server localy

Maven, Git and JDK should be installed

```
git clone https://github.com/codenjoyme/codenjoy-builder.git
cd codenjoy-build/
mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true clean jetty:run-war
```

If build failed for some reasons, change version of server to build in `pom.xml`:

```
     <groupId>com.codenjoy</groupId>
     <artifactId>codenjoy-builder</artifactId>
-    <version>1.0.22</version>
+    <version>1.0.23</version>
     <packaging>war</packaging>
```


## How to start your client

1. Register in Codenjoy server, remember your e-mail
2. Clone repo with client:
```
git clone https://github.com/codenjoyme/codenjoy.git
```
3. Install WebSocket gem (Ruby and `bundler` gem should be installed):
```
cd codenjoy/CodingDojo/games/bomberman/src/main/Ruby/
bundle install
```
4. Run your client:
```
ruby game.rb 127.0.0.1:8080 root1@localhost.local
```

Now you can code your bot, change code of `game.rb` after block
 
```

    ############################################################################################################
    #
    #                               YOUR ALGORITHM HERE
    #
    #    Set variables:
    #     * +act+ (true/false) - Place bomb or not in current iteration
    #     * +direction+ - Direction to move (UP, DOWN, LEFT, RIGHT)
    #
    ############################################################################################################

```

## Documentation for methods

To generate readable documentation for classes run YARD (`yard` gem should be installed)

```
# Doc will be placed in doc/index.html
yard doc game.rb
```