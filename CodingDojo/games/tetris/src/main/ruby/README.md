# Ruby client for Tetris game

## How to start your client

1. Register in Codenjoy server, remember your e-mail and copy code from board url
2. Read game instructions 
3. Install WebSocket gem (Ruby and `bundler` gem should be installed):
```
cd codenjoy/CodingDojo/games/bomberman/src/main/Ruby/
bundle install
```
4. Run your client:
```
ruby game.rb 127.0.0.1:8080 3edq63tw0bq4w4iem7nb 20010765231070354251
```

Now you can code your bot, change code of `solver.rb` after block
 
```

    #######################################################################
    #
    #                     YOUR ALGORITHM HERE
    #
    #######################################################################
```

## Documentation for methods

To generate readable documentation for classes run YARD (`yard` gem should be installed)

```
# Doc will be placed in doc/index.html
yard doc game.rb
```