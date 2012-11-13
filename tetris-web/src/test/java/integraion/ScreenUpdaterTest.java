package integraion;

import net.tetris.services.PlayerService;
import net.tetris.services.TimerService;
import org.apache.commons.collections.list.SynchronizedList;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * User: serhiy.zelenin
 * Date: 7/2/12
 * Time: 6:35 PM
 */
public class ScreenUpdaterTest {
    private Server server;
    private int port;
    private ServletContext servletContext;
    private WebApplicationContext applicationContext;
    private PlayerService playerService;
    private TimerService timerService;
    private HttpClient client;

    private static final long DEFAULT_TIMEOUT = 1000;
    private String serverUrl;
    private List<String> contents = SynchronizedList.decorate(new ArrayList<String>());
    private List<ContentExchange> contentExchanges = SynchronizedList.decorate(new ArrayList<ContentExchange>());
    private AtomicInteger expiredCount = new AtomicInteger(0);
    private AtomicInteger completedCount = new AtomicInteger(0);

    @Before
    public void setUp() throws Exception {
        this.port = start();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        playerService = applicationContext.getBean(PlayerService.class);
        timerService = applicationContext.getBean(TimerService.class);
        timerService.pause();

        client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setThreadPool(new ExecutorThreadPool(4, 256, DEFAULT_TIMEOUT, TimeUnit.SECONDS));
        client.setTimeout(DEFAULT_TIMEOUT);
        client.start();

        serverUrl = "http://localhost:" + port;
    }

    public int start() throws Exception {
        stop();

        server = new Server(0);

        WebAppContext context = loadWebContext();
        context.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                servletContext = sce.getServletContext();
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
            }
        });
        server.setHandler(context);
        server.start();

        return server.getConnectors()[0].getLocalPort();
    }

    @After
    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test(timeout = 1000)
    public void shouldBeAbleToProceedSeveralRequests() throws IOException, InterruptedException {
        playerService.addNewPlayer("testUser", serverUrl + "/test", null);

        sendForScreen(10, "testUser");
        Thread.sleep(50);
        playerService.nextStepForAllGames();

        sendForScreen(20, "testUser");
        Thread.sleep(50);
        playerService.nextStepForAllGames();

        sendForScreen(30, "testUser");
        Thread.sleep(50);
        playerService.nextStepForAllGames();

        while (!areAllDone()) {
            Thread.sleep(100);
        }

        assertEquals(0, expiredCount.get());
        assertEquals(60, completedCount.get());
    }

    private void sendForScreen(int times, String playerName) throws IOException {
        for (int i = 0; i < times; i++) {
            ContentExchange exchange = new ContentExchange() {
                @Override
                protected void onResponseComplete() throws IOException {
                    completedCount.incrementAndGet();
                    contents.add(this.getResponseContent());
                }

                @Override
                protected void onExpire() {
                    expiredCount.incrementAndGet();
                }
            };
            exchange.setMethod("GET");
            exchange.setURL(serverUrl + "/screen?" +playerName);
            contentExchanges.add(exchange);
            client.send(exchange);
        }
    }

    private boolean areAllDone() {
        for (ContentExchange contentExchange : contentExchanges) {
            if (!contentExchange.isDone()) {
                return false;
            }
        }
        return  true;
    }

    private WebAppContext loadWebContext() throws IOException {
        Collection<String> urls = Arrays.asList("src/main/webapp");
        for (String url : urls) {
            WebAppContext context = new WebAppContext(url, "");
            Resource resource = context.newResource(context.getWar());
            if (resource.exists()) {
                return context;
            }
        }
        throw new RuntimeException("Webapp not found!");
    }

}
