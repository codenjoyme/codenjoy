from player import Player
from codenjoy_connection import CodenjoyConnection


try:
    name = 'anatoliliotych' # name which was used during registration
    port = '8080' # game port
    host = 'localhost' # game host
    game_url = 'codenjoy-contest/ws?' # game url

    url = "ws://{0}:{1}/{2}user={3}".format(host, port, game_url, name)
    player = Player()
    ws = CodenjoyConnection(url, player)
    ws.connect()
    ws.run_forever()
except KeyboardInterrupt:
    ws.close()
