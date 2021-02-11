# Ruby client for Battlecity game

## How to start your client

1. Register in Codenjoy server, remember your e-mail and copy code from board url
2. Read game instructions
3. Install WebSocket gem (Ruby and `bundler` gem should be installed):
```
cd codenjoy/CodingDojo/games/battlecity/src/main/ruby/
bundle install
```
4. Run your client:
```
ruby runner.rb https://dojorena.io/codenjoy-contest/board/player/70xewaaa7ddy9yphm1u0?code=2603499961919438773
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
