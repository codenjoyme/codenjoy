from ws4py.client.threadedclient import WebSocketClient

class CodenjoyConnection(WebSocketClient):
    def __init__(self, url, player):
        super(CodenjoyConnection, self).__init__(url)
        self.player = player

    def received_message(self, m):
        print "Received from server: %s" % (str(m))
        self.player.process_data(m)
        self.send(self.player.make_step())

