package com.codenjoy.dojo.transport;

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
