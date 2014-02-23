package com.codenjoy.dojo.services.chat;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
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
        this(calendar.now(), null, systemMessage);
    }

    public ChatMessage(Date time, String playerName, String message) {
        this.message = message;
        this.time = time;
        this.playerName = playerName;
    }

    public ChatMessage(String playerName, String message) {
        this(calendar.now(), playerName, message);
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
        if (time == null) {
            return "";
        }

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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

    public class ChatMessageReader {
        public String getDate() {
            if (ChatMessage.this.time == null) {
                return "";
            }
            return sdf.format(ChatMessage.this.time);
        }

        public String getPlayer() {
            return ChatMessage.this.playerName;
        }

        public String getMessage() {
            return ChatMessage.this.message;
        }
    }

    public static class ChatMessageBuilder {

        private String message;
        private String date;
        private String player;

        public void setMessage(String message) {
            this.message = message;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public ChatMessage getChatMessage() {
            try {
                Date parse = ("".equals(date))?null:sdf.parse(date);
                return new ChatMessage(parse, player, message);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
