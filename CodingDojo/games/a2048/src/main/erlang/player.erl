-module(player).
-export([
         step/1,
         username/0,
         hostname/0,
         gameurl/0
        ]).

userid() ->
  "3edq63tw0bq4w4iem7nb".
code() ->
  "1234567890123456789".
hostname() ->
  "codenjoy.com:80".
gameurl() ->
  "codenjoy-contest/ws?".

step(Msg) ->
 Arr = utilities:process_msg(Msg),
 io:format("~p~n",[Arr]),
  "right=1".
  %%"some game specific response". %% Specify your response here
