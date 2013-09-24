package com.codenjoy.dojo.services.chat;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: sanja
 * Date: 25.09.13
 * Time: 0:55
 */
public class ChatMessage {

    static DateCalendar calendar = new DateCalendar() {
        @Override
        public Date now() {
            return Calendar.getInstance().getTime();
        }
    };

    private Date time;
    private String playerName;
    private String message;

    public ChatMessage(String systemMessage) {
        this.message = systemMessage;
        this.time = calendar.now();
    }

    public ChatMessage(String playerName, String message) {
        this(message);
        this.playerName = playerName;
    }

    public boolean contains(String message) {
        return this.message.contains(message);
    }

    public boolean is(String playerName) {
        return this.playerName != null && this.playerName.equals(playerName);
    }

    @Override
    public String toString() {
        if (playerName == null) {
            return message;
        }

        return String.format("[%s] %s: %s", getDate(), playerName, message);
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        int days = Days.daysBetween(new DateTime(time), new DateTime(calendar.now())).getDays();
        String date = sdf.format(time);
        if (days > 0) {
            date = days + " days ago, " + date;
        }
        return date;
    }

    public boolean isSystem() {
        return this.playerName == null;
    }
}
