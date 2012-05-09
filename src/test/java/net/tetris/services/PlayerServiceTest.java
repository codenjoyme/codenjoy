package net.tetris.services;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration(locations = {"classpath:/net/tetris/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private FakeHttpServer server;
    
    @Autowired
    private PlayerService playerService;

    @Before
    public void setUp() throws IOException {
        server = new FakeHttpServer(1111);
        server.start();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    @Ignore
    public void shouldAddNewPlayer(){
        Player player = playerService.addNewPlayer("vasya", "http://localhost:1111");

        playerService.nextStepForAllGames();

//        player.makeNextStep();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        String request = server.getRequest();
        System.out.println("request = " + request);
    } 
}
