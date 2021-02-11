package main

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
