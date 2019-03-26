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
﻿#include <iostream>
#include "utils/RandomDice.h"

#include "DumbDirectionSolver.h"
#include "YourDirectionSolver.h"

/////////////////////////////////////////////
// Change your name from "_bot_" to your one;
char* yourName = "_bot_";
/////////////////////////////////////////////
// This is switcher between our and your bot.
// Good luck !!!
#if 0
typedef DumbDirectionSolver Solver;
#else
typedef YourDirectionSolver Solver;
#endif
/////////////////////////////////////////////

#include "WebClient.h"

int main(int argc, char** argv) {
	int EXITCODE = 0;

	RandomDice* rd = new RandomDice();
	
	DirectionSolver* dds = new Solver(rd);

	WebClient wcl(dds);
	try {
		wcl.run("ws://codenjoy.com:80/codenjoy-contest/ws", yourName);
	} catch (const std::runtime_error& ex) {
		std::cout << "Runtime error happened: " << ex.what() << std::endl;
		EXITCODE = 1;
	} catch (const std::invalid_argument& iarex) {
		std::cout << "Invalid argument: " << iarex.what() << std::endl;
		EXITCODE = 2;
	} catch (websocketpp::lib::error_code e) {
		std::cout << e.message() << std::endl;
	} catch (...) {
		std::cout << "Something's definitely not right..." << std::endl;
		EXITCODE = 3;
	}
	std::cout << "Done";
	std::cin.get();

	delete dds;
	delete rd;

	return EXITCODE;
}
