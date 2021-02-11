package com.codenjoy.dojo.services.dao;

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


import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PaymentTest {

    private static Payment service;

    @Before
    public void setup() {
        String dbFile = "target/payment.db" + new Random().nextInt();
        service = new Payment(
                new SqliteConnectionThreadPoolFactory(false, dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }));
    }

    @After
    public void tearDown() {
        service.removeDatabase();
    }

    @Test
    public void shouldCanPlay_whenBuy() throws InterruptedException {
        assertFalse(service.canPlay("user", "game"));

        service.buy("user", "game2", 100);

        assertFalse(service.canPlay("user", "game"));

        service.buy("user", "game", 100);

        assertTrue(service.canPlay("user", "game"));
    }

    @Test
    public void shouldTill_whenBuy() throws InterruptedException {
        long now = Calendar.getInstance().getTime().getTime();
        assertEquals(0, service.till("user", "game"));

        service.buy("user", "game", 365);

        assertEquals(365, delta(now, service.till("user", "game")));
    }

    private long delta(long now, long till) {
        if (till == 0) {
            return 0;
        }
        return Math.round((double)(till - now)/1000/60/60/24);
    }

    @Test
    public void shouldBuyTwice() throws InterruptedException {
        long now = Calendar.getInstance().getTime().getTime();

        assertEquals(0, delta(now, service.till("user", "game")));

        service.buy("user", "game", 100);

        assertEquals(100, delta(now, service.till("user", "game")));

        service.buy("user", "game", 170);

        assertEquals(270, delta(now, service.till("user", "game")));
    }
}
