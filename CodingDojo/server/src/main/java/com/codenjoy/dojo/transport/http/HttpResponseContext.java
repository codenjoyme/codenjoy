package com.codenjoy.dojo.transport.http;

import org.eclipse.jetty.client.Address;

public class HttpResponseContext {

    public HttpResponseContext(int responseStatus, String requestURI, Address address) {
        this.requestURI = requestURI;
        this.responseStatus = responseStatus;
        this.address = address;
    }

    private String requestURI;
    private int responseStatus;
    private Address address;

    public String getRequestURI() {
        return requestURI;
    }


    public int getResponseStatus() {
        return responseStatus;
    }

    public Address getAddress() {
        return address;
    }
}
