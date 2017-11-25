package com.codenjoy.dojo.transport.screen.async;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.screen.ScreenSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

@Component
public class RestScreenSender implements ScreenSender<ScreenRecipient, ScreenData>, AsyncListener {
    private List<UpdateRequest> requests = new ArrayList<UpdateRequest>();
    private Set<AsyncContext> timedOutRequests = Collections.synchronizedSet(new HashSet<AsyncContext>());

    private PlayerDataSerializer serializer;

    private ScheduledExecutorService restSenderExecutorService;

    private static Logger logger = LoggerFactory.getLogger(RestScreenSender.class);

    @Autowired
    public RestScreenSender(ScheduledExecutorService restSenderExecutorService,
                            PlayerDataSerializer serializer) {
        this.restSenderExecutorService = restSenderExecutorService;
        this.serializer = serializer;
    }

    public synchronized void scheduleUpdate(UpdateRequest updateRequest) {
        logger.debug("Scheduled screen update {}. Context: {}",
                updateRequest, updateRequest.getAsyncContext());
        Iterator<UpdateRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            UpdateRequest request = iterator.next();
            if (timedOutRequests.contains(request.getAsyncContext())) {
                iterator.remove();
            }
        }
        requests.add(updateRequest);
    }

    @Override
    public synchronized void sendUpdates(final Map<ScreenRecipient, ScreenData> playerScreens) {
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();

        for (final UpdateRequest updateRequest : requests) {
            tasks.add(new PlayerScreenSendCallable(updateRequest, playerScreens));
        }
        logger.debug("Executing {} screen updates", tasks.size());
        try {
            restSenderExecutorService.invokeAll(tasks, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for all glass data to be sent on client.", e);
        }
        requests.clear();
    }

    private void sendUpdateForRequest(Map<ScreenRecipient, ScreenData> playerScreens, UpdateRequest updateRequest) {
        AsyncContext asyncContext = updateRequest.getAsyncContext();
        ServletResponse response = asyncContext.getResponse();
        try {
            PrintWriter writer = getPrintWriter(response);
            serializer.writeValue(writer, playerScreens);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            asyncContext.complete();
        }
    }

    private PrintWriter getPrintWriter(ServletResponse response) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        // return response.getWriter();
        // TODO обманом сделал все ответы серверу сжатыми в формате gzip
        response.getClass().getDeclaredMethod("addHeader", String.class, String.class).invoke(response, "Content-Encoding", "gzip");
        OutputStream outputStream = response.getOutputStream();
        return new PrintWriter(new MyCollectOutputStream(new GZIPOutputStream(outputStream)));
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.debug("Context timed out: {}", event.getAsyncContext());
        timedOutRequests.add(event.getAsyncContext());
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
    }

    private class PlayerScreenSendCallable implements Callable<Void> {
        private final UpdateRequest updateRequest;
        private final Map<ScreenRecipient, ScreenData> playerData;

        public PlayerScreenSendCallable(UpdateRequest updateRequest, Map<ScreenRecipient, ScreenData> playerData) {
            this.updateRequest = updateRequest;
            this.playerData = playerData;
        }

        @Override
        public Void call() {
            Map<ScreenRecipient, ScreenData> playersToUpdate = findScreensFor(updateRequest, playerData);
            if (playersToUpdate.isEmpty()) {
                updateRequest.getAsyncContext().complete();
                return null;
            }
            sendUpdateForRequest(playersToUpdate, updateRequest);
            return null;
        }

        private Map<ScreenRecipient, ScreenData> findScreensFor(UpdateRequest updateRequest, Map<ScreenRecipient, ScreenData> playerData) {
            HashMap<ScreenRecipient, ScreenData> result = new HashMap<ScreenRecipient, ScreenData>();
            for (Map.Entry<ScreenRecipient, ScreenData> entry : playerData.entrySet()) {
                if (updateRequest.isApplicableFor(entry.getKey())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        }

    }

}

