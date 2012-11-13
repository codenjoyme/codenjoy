package net.tetris.services;

import javax.servlet.AsyncContext;

/**
 * User: serhiy.zelenin
 * Date: 11/12/12
 * Time: 5:18 PM
 */
public abstract class UpdateRequest {
    protected AsyncContext asyncContext;

    public UpdateRequest(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public abstract boolean isApplicableFor(Player player);
}
