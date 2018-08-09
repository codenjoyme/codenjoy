/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
﻿#ifndef WEBCLIENT_H
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
