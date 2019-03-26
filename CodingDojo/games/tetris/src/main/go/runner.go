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
package main

import (
	"./network"
	"flag"
	"log"
	"os"
	"os/signal"
	"strings"
)

var url = "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890";

func main() {
	flag.Parse()
	log.SetFlags(0)

	url = strings.Replace(url, "http", "ws", -1)
	url = strings.Replace(url, "board/player/", "ws?user=", -1)
	url = strings.Replace(url, "?code=", "&code=", -1)

	network.NewClient(url)

	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)
	<-interrupt
	log.Println("exiting")
	// To cleanly close a connection, a client should send a close
	// frame and wait for the server to close the connection.
}
