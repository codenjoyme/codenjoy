package com.codenjoy.dojo.transport.http;

import org.eclipse.jetty.client.Address;

/**
 * User: serhiy.zelenin
 * Date: 3/21/13
 * Time: 6:18 PM
 */
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
