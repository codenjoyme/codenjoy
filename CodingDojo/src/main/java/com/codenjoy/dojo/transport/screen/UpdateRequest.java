package com.codenjoy.dojo.transport.screen;

import javax.servlet.AsyncContext;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:08 PM
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
