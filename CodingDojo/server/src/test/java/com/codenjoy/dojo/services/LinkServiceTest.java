package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkServiceTest {

    private LinkService service;

    @Before
    public void setup() {
        service = new LinkService();
    }

    @Test
    public void shouldGetDataByLinkForOneStorage() {
        // when
        LinkService.LinkStorage storage = service.forLink();
        String link = storage.getLink();
        storage.getMap().put("key", "value");

        // then
        assertEquals("{key=value}", service.getData(link).toString());
    }

    @Test
    public void shouldAllStorageAllIndependent() {
        // when
        LinkService.LinkStorage storage1 = service.forLink();
        String link1 = storage1.getLink();
        storage1.getMap().put("key", "value");

        LinkService.LinkStorage storage2 = service.forLink();
        String link2 = storage2.getLink();
        storage2.getMap().put("key2", "value2");

        // then
        assertEquals("{key=value}", service.getData(link1).toString());
        assertEquals("{key2=value2}", service.getData(link2).toString());
    }

    @Test
    public void shouldStorageNotFoundByLink() {
        // when
        // then
        assertEquals(null, service.getData("bad_link"));
    }
}