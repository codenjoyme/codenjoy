package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
