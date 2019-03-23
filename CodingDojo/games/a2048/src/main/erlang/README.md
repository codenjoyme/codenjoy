Tetris client is based on Websocket client.

Websocket client implementation was used from [Repo](https://github.com/jeremyong/websocket_client).

For configuration:
1. update player.erl with proper userid, code, hostname and game url

For installation:
```
  make
```

For running:
```
  cd ebin
  erl -s -run ws_handler
```

