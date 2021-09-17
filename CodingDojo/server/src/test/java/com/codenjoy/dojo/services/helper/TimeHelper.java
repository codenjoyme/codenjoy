package com.codenjoy.dojo.services.helper;

import com.codenjoy.dojo.services.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Lazy
@Component
@AllArgsConstructor
public class TimeHelper {

    @Autowired
    private TimeService time;

    public void nowIs(long time) {
        this.time.set(() -> time);
    }

    public void removeAll() {
        this.time.set(() -> Calendar.getInstance().getTimeInMillis());
    }
}
