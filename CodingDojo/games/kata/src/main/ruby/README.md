# Ruby client for Kata game

## How to start your client

1. Register in Codenjoy server, remember your e-mail and copy code from board url
2. Read game instructions 
3. Install WebSocket gem (Ruby and `bundler` gem should be installed):
```
Unzip kata archire and move to it's root folder.
bundle install
```
4. Run your client:
```
ruby runner.rb http://dojorena.com/codenjoy-contest/board/player/rmtsf91ee1xitijsdtxz\?code\=7743500744161891908
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
