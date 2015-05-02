package com.codenjoy.dojo.services.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PaymentTest {

    private static Payment service;

    @Before
    public void setup() {
        service = new Payment("target/payment.db" + new Random().nextInt());
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
        return (till - now)/1000/60/60/24;
    }

    @Test
    public void shouldByuTwice() throws InterruptedException {
        long now = Calendar.getInstance().getTime().getTime();

        assertEquals(0, delta(now, service.till("user", "game")));

        service.buy("user", "game", 100);

        assertEquals(100, delta(now, service.till("user", "game")));

        service.buy("user", "game", 170);

        assertEquals(270, delta(now, service.till("user", "game")));
    }
}
