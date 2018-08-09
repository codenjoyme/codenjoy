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
#ifndef UTILS_H
#define UTILS_H

#include <algorithm>
#include <cmath>
#include <cstdlib>
#include <list>
#include <map>
#include <tuple>
#include <memory>
#include <sstream>
#include <string>
#include <stdexcept>

#ifdef _WIN32
#define LL(x) L## x

typedef wchar_t Char;
typedef std::wstring String;
typedef std::wstringstream StringStream;

#else // Assume linux
#define LL(x) x
typedef char Char;
typedef std::string String;
typedef std::stringstream StringStream;

#endif

typedef std::map<String, Char> ElementMap;
typedef std::pair<String, Char> ElementItem;


typedef std::tuple<int, int, int> DirectionVector; // Direction vector: value, dx, dy
typedef std::pair<String, DirectionVector> DirectionItem;
typedef std::map<String, DirectionVector> DirectionMap;

#endif
