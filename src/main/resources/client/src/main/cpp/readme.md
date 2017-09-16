# Expansion AI Bot written in C++

## Installation Instructions

### 1. Install `vcpkg`

`vcpkg` is not a part of VS. It should be installed separately. Here are steps:

####  1.1 Checkout `vcpkg` [source code](https://github.com/Microsoft/vcpkg)
  
  Prerequisites are:  
  * Windows 10, 8.1, or 7  
  * Visual Studio 2017 or Visual Studio 2015 Update 3  
  * Git

####  1.2 Run command  
  `> bootstrap-vcpkg.bat`  
  It will build vcpkg tool

####  1.3 Run command  
  `> vcpkg integrate install`
  
### 2. Install C++ REST SDK  
It will install C++ REST SDK with dependencies (including boost)  
Please note, that they appear to be installed globally – so __the packages will be available for any project on the machine__.

---

## Short application description

### The main file `Expansion.cpp`  
It instantiates `WebSocketClient` class instance.

`WebSocketClient` constructor requires 2 `wstring` parameters:  
  * The game server url (e.g. ws://127.0.0.1:8080/codenjoy-contest/ws)  
  * The user email, which is used to distinguish the bot  

Also it accepts a template parameter – a `MessageHandler` class, which will be used to handle messages and generate responses.  
An example of such class is in `StringMessageHandler.h` file.

### `MessageHandler` class is expected to have:  
  * public parameterless constructor  
  * public method `std::string Handle(const std::string& message, bool& finish)`  

Method `Handle(...)` is the method, which actually processes the message. This is __an entry point for each bot logic action__.

It accepts 2 parameters:
  * `std::string` input `message` in UTF-8 encoding.
  * `bool` parameter `finish` by the reference. Handler can set it to true. When it is set to true – `WebSocketClient` finishes work and closes the console.

It returns the output message for the game server as `std::string` in UTF-8 encoding.

### `WebSocketClient` implementation in `WebSocketClient.h` file.
It connects to the game server and registers WebSocket handler for incoming messages.
