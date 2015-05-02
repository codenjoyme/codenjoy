package com.codenjoy.dojo.transport;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;

public class MockAsyncContext implements AsyncContext {
    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private boolean completed;
    private String error;

    public MockAsyncContext(MockHttpServletResponse response) {
        this(null, response);
    }

    public MockAsyncContext(MockHttpServletRequest request, MockHttpServletResponse response) {
        this.response = response;
        this.request = request;
    }

    @Override
    public ServletRequest getRequest() {
        return request;
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispatch() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispatch(String path) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispatch(ServletContext context, String path) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void complete() {
        if (completed) {
            error = "AsyncContext already completed!";
        }
        completed = true;
    }

    @Override
    public void start(Runnable run) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(AsyncListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimeout(long timeout) {
    }

    @Override
    public long getTimeout() {
        return 0;
    }

    public boolean isComplete() {
        return completed;
    }

    public String getError() {
        return error;
    }
}
