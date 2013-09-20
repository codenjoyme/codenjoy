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