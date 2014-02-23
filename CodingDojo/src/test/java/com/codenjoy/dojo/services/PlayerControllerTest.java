package com.codenjoy.dojo.services;

import com.codenjoy.dojo.JettyRunner;
import org.junit.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:30 AM
 */
public class PlayerControllerTest {

    private static WebSocketRunner client;

    private static String url;

    private static WsPlayerController controller;
    private static PlayerService players;
    private static JettyRunner runner;

    private static Joystick joystick;
    private static Player player;

    private static final String SERVER = "ws://127.0.0.1:8081/codenjoy-contest/ws";
    private static String USER_NAME = "apofig";

    private static List<String> commands = new LinkedList<String>();

    @BeforeClass
    public static void setupJetty() throws Exception {
        runner = new JettyRunner("src/main/webapp");
        runner.spy("playerService");
        int port = runner.start("/codenjoy-contest", 8081);

        url = "http://localhost:" + port + "/codenjoy-contest/";
        System.out.println("web application started at: " + url);

        players = runner.getBean(PlayerService.class, "playerService");
        controller = runner.getBean(WsPlayerController.class, "wsPlayerController");

        joystick = new Joystick() {
            @Override
            public void down() {
                commands.add("down");
            }

            @Override
            public void up() {
                commands.add("up");
            }

            @Override
            public void left() {
                commands.add("left");
            }

            @Override
            public void right() {
                commands.add("right");
            }

            @Override
            public void act(int... p) {
                commands.add("act" + Arrays.toString(p));
            }
        };

        player = new Player(USER_NAME, "password", "127.0.0.1", "game",
                PlayerScores.NULL, Information.NULL, Protocol.WS);

        controller.registerPlayerTransport(player, joystick);

        client = WebSocketRunner.run(SERVER, USER_NAME);
    }

    @Before
    public void clean() {
        client.reset();
        commands.clear();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.stop();
    }

    @Test
    public void shouldLeft() {
        client.willAnswer("LEFT");
        waitForPlayerResponse();

        assertEquals("[left]", commands.toString());
    }

    @Test
    public void shouldRight() {
        client.willAnswer("right");
        waitForPlayerResponse();

        assertEquals("[right]", commands.toString());
        clean();
    }

    @Test
    public void shouldUp() {
        client.willAnswer("Up");
        waitForPlayerResponse();

        assertEquals("[up]", commands.toString());
        clean();
    }

    @Test
    public void shouldAct() {
        client.willAnswer("aCt");
        waitForPlayerResponse();

        assertEquals("[act[]]", commands.toString());
        clean();
    }

    @Test
    public void shouldActWithParameters() {
        client.willAnswer("ACt(1,2 ,3, 5)");
        waitForPlayerResponse();

        assertEquals("[act[1, 2, 3, 5]]", commands.toString());
        clean();
    }

    @Test
    public void shouldDown() {
        client.willAnswer("DowN");
        waitForPlayerResponse();

        assertEquals("[down]", commands.toString());
        clean();
    }

    @Test
    public void shouldRightAct() {
        client.willAnswer("right,Act");
        waitForPlayerResponse();

        assertEquals("[right, act[]]", commands.toString());
        clean();
    }

    @Test
    public void shouldMixed() {
        client.willAnswer("Act,right, left ,act");
        waitForPlayerResponse();

        assertEquals("[act[], right, left, act[]]", commands.toString());
        clean();
    }

    @Test
    public void shouldCheckRequest() {
        client.willAnswer("act");
        waitForPlayerResponse();

        assertEquals("board=some-request", client.getRequest());
    }

    private void waitForPlayerResponse() {
        try {
            controller.requestControl(player, "some-request");
            while (commands.isEmpty()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}