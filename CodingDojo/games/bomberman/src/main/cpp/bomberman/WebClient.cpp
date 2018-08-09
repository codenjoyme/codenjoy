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
﻿#include "WebClient.h"


WebClient::WebClient(DirectionSolver* ds) : solver(ds)   {
	if (solver == nullptr) {
		throw std::invalid_argument("Solver cant be uninitialised!");
	}
	client.clear_access_channels(websocketpp::log::alevel::all);
	client.clear_error_channels(websocketpp::log::elevel::all);
	client.init_asio();
	client.set_message_handler(bind(&WebClient::onMessage, this, &client, ::_1, ::_2));
}


void WebClient::run(std::string server, std::string user) {
	serverName = server;
	userName = user;

	size_t rtrs = 0;
	while (true) {
		connect();
		std::cout << "Retrying: " << ++rtrs << std::endl;
		if (rtrs > 10 ) {
			throw std::runtime_error("WebClient::run(...): Connection problems");
		}
	}
}

bool WebClient::connect() {
	std::string connectionString = serverName + "?user=" + userName;
	
	websocketpp::lib::error_code err;
	Client::connection_ptr con = client.get_connection(connectionString, err);
	client.connect(con);
	size_t status = client.run();
	return status;
}

void WebClient::onMessage(Client* c, websocketpp::connection_hdl hdl, message_ptr pMsg) {
	Client::connection_ptr con = c->get_con_from_hdl(hdl);
	std::string res_s = con->get_resource();
	if (res_s != "/codenjoy-contest/ws?user=" + userName) {
		res_s = "WebClient::onMessage(...) Server answer is not right: " + res_s;
		throw std::runtime_error(res_s.c_str());
	}
	std::string buffer_got = pMsg->get_payload();
	String boardString;
#ifdef _WIN32
	boardString.resize(MultiByteToWideChar(CP_UTF8, 0, &buffer_got[0], buffer_got.length(),
													   NULL, 0));
	MultiByteToWideChar(CP_UTF8, 0, &buffer_got[0], buffer_got.length(),
									&boardString[0], boardString.capacity());
#else //Assume linux
	boardString = buffer_got;
#endif 

	if (boardString.substr(0, 6) == LL("board=")) {
		boardString = boardString.substr(6, boardString.length() - 6);
		while (*boardString.rbegin() == LL('\0')) {
			boardString.pop_back();
		}

		Board b(boardString);

		String answer = solver->get(b);
		std::string utf_answer;
#ifdef _WIN32
		utf_answer.resize(WideCharToMultiByte(CP_UTF8, 0, &answer[0], answer.length(),
											   NULL, 0,  NULL, NULL));

		WideCharToMultiByte(CP_UTF8, 0, &answer[0], answer.length(),
										&utf_answer[0], utf_answer.capacity(), NULL, NULL);
		if (utf_answer == "") { // This happens if bomberman's still dead
			if (answer != LL("")) {
				throw std::runtime_error("WebClient::onMessage(...): Conversion from Char to utf8 error!");
			}
			return;
		}
#else // Assume linux
		utf_answer = answer;
#endif
		
		pMsg->set_opcode(websocketpp::frame::opcode::text);
		pMsg->set_payload(utf_answer);

		std::cout << "Sending ACTION: " << pMsg->get_payload() << std::endl;
		websocketpp::lib::error_code err;
		c->send(hdl, pMsg->get_payload(), pMsg->get_opcode(), err);
	}
}
