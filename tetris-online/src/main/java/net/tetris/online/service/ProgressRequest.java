package net.tetris.online.service;

import javax.servlet.AsyncContext;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 2:01 PM
 */
public class ProgressRequest {
    private AsyncContext asyncContext;
    private String timestamp;

    public ProgressRequest(AsyncContext asyncContext, String timestamp) {
        this.asyncContext = asyncContext;
        this.timestamp = timestamp;
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
