-module(ws_handler).

-behaviour(websocket_client_handler).
-export([
         start/0,
         init/2,
         websocket_info/3,
         websocket_handle/3,
         websocket_terminate/3
        ]).

-define(Host,player:hostname()).
-define(User,player:userid()).
-define(Code,player:code()).
-define(GameUrl,player:gameurl()).

start() ->
  websocket_client:start_link("ws://"++ ?Host ++ "/"++ ?GameUrl ++ "user=" ++ ?User ++ "code=" ++ ?Code, ?MODULE, []).

init([], _ConnState) ->
    websocket_client:cast(self(), {text, <<"message 1">>}),
    {ok, 2}.

websocket_handle({text, Msg}, _ConnState, State) ->
    io:format("Received msg ~p~n", [Msg]),
    Step = list_to_binary(player:step(Msg)),
    {reply, {text, Step}, State}.

websocket_info(start, _ConnState, State) ->
    {reply, {text, <<"erlang message received">>}, State}.

websocket_terminate(Reason, _ConnState, State) ->
    io:format("Websocket closed in state ~p wih reason ~p~n",
              [State, Reason]),
    ok.
