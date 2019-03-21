.PHONY: all clean

all:
	mkdir -p lib
	cd lib && git clone https://github.com/jeremyong/websocket_client
	cd ..
	mkdir -p ebin
	erlc -o ebin -pa ebin lib/websocket_client/src/*.erl
	erlc -o ebin -pa ebin utilities.erl	
	erlc -o ebin -pa ebin player.erl
	erlc -o ebin -pa ebin ws_handler.erl

clean:
	rm -rf ebin lib
