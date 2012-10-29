package net.tetris.online.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 2:01 PM
 */
@Component
public class RestDataSender {
    private static Logger logger = LoggerFactory.getLogger(RestDataSender.class);

    private ScheduledExecutorService restSenderExecutorService;
    private final ObjectMapper objectMapper;
    private final ArrayList<ProgressRequest> requests;

    @Autowired
    public RestDataSender(ScheduledExecutorService restSenderExecutorService) {
        objectMapper = new ObjectMapper();
        this.restSenderExecutorService = restSenderExecutorService;
        requests = new ArrayList<>();
    }

    public synchronized void scheduleGameProgressRequest(ProgressRequest progressRequest) {
        requests.add(progressRequest);
    }

    public synchronized void sendProgressFor(String playerName, String timestamp, int progress) {
        List<Callable<Void>> tasks = new ArrayList<>();

        Iterator<ProgressRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            ProgressRequest request = iterator.next();
            String loggedUser = (String) request.getAsyncContext().getRequest().getAttribute(SecurityFilter.LOGGED_USER);
            if (playerName.equals(loggedUser) && timestamp.equals(request.getTimestamp())) {
                tasks.add(new ProgressCallable(request, progress));
                iterator.remove();
            }
        }
        try {
            restSenderExecutorService.invokeAll(tasks, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for all glass data to be sent on client.", e);
        }

    }

    private class ProgressCallable implements Callable<Void> {
        private ProgressRequest request;
        private int progress;

        public ProgressCallable(ProgressRequest request, int progress) {
            this.request = request;
            this.progress = progress;
        }

        @Override
        public Void call() throws Exception {
            AsyncContext asyncContext = request.getAsyncContext();
            ServletResponse response = asyncContext.getResponse();
            try {
                PrintWriter writer = response.getWriter();
                objectMapper.writeValue(writer, Collections.singletonMap("progress", progress));

            } catch (Exception e) {
                logger.error("Can't send progress", e);
            } finally {
                asyncContext.complete();
            }
            return null;
        }
    }
}
