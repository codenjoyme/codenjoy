package com.codenjoy.dojo.lemonade.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

public class ServerMessagesManager {

    private final String invalidCommandMessage =
            "Invalid input. lemonadeToMake parameter should be in [0, 1000] range.\n" +
                    "signsToMake parameter should be in [0, 50] range.\n" +
                    "lemonadePriceCents parameter should be in [0, 100] range.\n";

    private boolean isCommandInvalid;
    private String statusMessages;
    private String reportMessages;
    private String morningMessages;


    public ServerMessagesManager() {
        isCommandInvalid = false;
    }

    public void reset() {
        isCommandInvalid = false;
    }

    public void setCommandInvalid(boolean isInvalid) {
        isCommandInvalid = isInvalid;
    }

    public void setMessages(String statusMessages, String reportMessages, String morningMessages) {
        this.statusMessages = statusMessages;
        this.reportMessages = reportMessages;
        this.morningMessages = morningMessages;
    }

    public String getMessages() {
        if (isCommandInvalid) {
            return invalidCommandMessage;
        }

        return String.join("", this.statusMessages, this.reportMessages, this.morningMessages);
    }
}
