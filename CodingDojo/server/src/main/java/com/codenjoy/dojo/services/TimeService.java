package com.codenjoy.dojo.services;

import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class TimeService {

    public long now() {
        return Calendar.getInstance().getTimeInMillis();
    }

}
