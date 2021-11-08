package main

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import (
	"flag"
	"fmt"
	"log"
	"os"
	"os/signal"
	"runtime/pprof"
	"strings"
	"time"

	"github.com/arukim/expansion/network"
)

var cpuprofile = flag.String("cpuprofile", "", "write cpu profile to file")
var memprofile = flag.String("memprofile", "", "write memory profile to this file")
var urlStr = flag.String("url", "ws://127.0.0.1:8080/codenjoy-contest/ws?user=1@a.com", "server url")

func main() {
	//"ws://codenjoy.com:80/codenjoy-contest/ws?user=your@email.com"
	flag.Parse()
	log.SetFlags(0)

	urls := strings.Split(*urlStr, " ")

	for id, url := range urls {
		network.NewClient(id, url)
	}

	if *memprofile != "" {
		go func() {
			for {
				time.Sleep(time.Second * 2)
				f, err := os.Create(*memprofile)
				if err != nil {
					log.Fatal(err)
				}
				pprof.WriteHeapProfile(f)
				f.Close()
			}
		}()
	}

	if *cpuprofile != "" {
		go func() {
			for {
				f, err := os.Create(*cpuprofile)
				if err != nil {
					log.Fatal(err)
				}

				fmt.Println("CPU profile start")
				pprof.StartCPUProfile(f)
				time.Sleep(time.Second * 30)
				pprof.StopCPUProfile()
				fmt.Println("CPU profile done")
				f.Close()
			}
		}()
	}

	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)
	<-interrupt
	log.Println("exiting")
	// To cleanly close a connection, a client should send a close
	// frame and wait for the server to close the connection.

}
