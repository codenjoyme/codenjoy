package com.codenjoy.dojo.transport.screen;

import javax.servlet.AsyncContext;

public abstract class UpdateRequest {
    protected AsyncContext asyncContext;

    public UpdateRequest(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public abstract boolean isApplicableFor(ScreenRecipient recipient);
}
