-module(player).
-export([
         step/1,
         username/0,
         hostname/0,
         gameurl/0
        ]).

username() ->
  "anatoliliotych".
hostname() ->
  "localhost:8080".
gameurl() ->
  "codenjoy-contest/ws?".

step(Msg) ->
 Arr = utilities:process_msg(Msg),
 io:format("~p~n",[Arr]),
  "right=1".
  %%"some game specific response". %% Specify your response here
