#ifndef WEBCLIENT_H
#define WEBCLIENT_H


#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include "utils/utils.h"
#include "DirectionSolver.h"
#include "utils/Board.h"

#ifdef _WIN32
#include <Windows.h>
#endif

using websocketpp::lib::placeholders::_1;
using websocketpp::lib::placeholders::_2;
using websocketpp::lib::bind;

typedef websocketpp::client<websocketpp::config::asio_client> Client;
typedef websocketpp::config::asio_client::message_type::ptr message_ptr;

class WebClient
{
public:
	WebClient(DirectionSolver* ds);
	void run(std::string server, std::string user);

private:
	bool connect();
	void onMessage(Client* c, websocketpp::connection_hdl hdl, message_ptr pMsg);
	Client client;
	DirectionSolver* solver;
	std::string serverName;
	std::string userName;
};

#endif