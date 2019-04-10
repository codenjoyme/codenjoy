-module(utilities).
-export([
      process_msg/1
    ]).

process_msg(Msg) ->
  binary:split(Msg,<<"&">>,[global]).
