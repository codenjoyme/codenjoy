TARGET = bomberman
PROJ = ./bomberman
UTIL = $(PROJ)/utils

CC = g++
FDEBUG = -ggdb3 -O0 -w
IFLAGS = -I $(PROJ)/third_party
CFLAGS = $(FDEBUG) -std=c++11 -fpermissive -D_WEBSOCKETPP_CPP11_STL_ -pthread 
LDIR = -L $(PROJ)/third_party/lib
LFLAGS = -lboost_thread -lboost_random -lboost_regex -lboost_date_time -lboost_chrono -lboost_system

util_c = $(wildcard $(UTIL)/*.cpp)  
util_h = $(wildcard $(UTIL)/*.h)
proj_c = $(wildcard $(PROJ)/*.cpp)  
proj_h = $(wildcard $(PROJ)/*.h)
objects = 

all: $(TARGET)

$(TARGET): objs
	$(CC) $(CFLAGS)  $(IFLAGS) -o bomberman.exe $(objects) $(LDIR) $(LFLAGS)
objs:
	$(CC) $(CFLAGS) $(IFLAGS) $(PROJ)/main.cpp -c $(util_c) $(util_h) $(proj_c) $(proj_h) $(LDIR) $(LFLAGS)
	$(eval objects := $(wildcard *.o))
clean: 
	rm -f *.o bomberman.exe
