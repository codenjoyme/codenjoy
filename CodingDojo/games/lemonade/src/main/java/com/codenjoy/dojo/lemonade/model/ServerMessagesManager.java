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

    private final String gameOverMessage =
            "YOU WON. GAME IS OVER.\nPLEASE, SEND 'RESET' COMMAND IN ORDER TO START A NEW GAME.\n\n\n";

    private boolean isCommandInvalid;
    private boolean isGameOver;
    private String statusMessages;
    private String reportMessages;
    private String morningMessages;

    public ServerMessagesManager() {
        isCommandInvalid = false;
    }

    public void reset() {
        isCommandInvalid = false;
        isGameOver = false;
    }

    public void setCommandInvalid(boolean isInvalid) {
        isCommandInvalid = isInvalid;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void setMessages(String statusMessages, String reportMessages, String morningMessages) {
        this.statusMessages = statusMessages;
        this.reportMessages = reportMessages;
        this.morningMessages = morningMessages;
    }

    public String getMessages() {

        String statusMsg = this.statusMessages;
        String reportMsg = this.reportMessages;
        String morningMsg = this.morningMessages;

        if (this.isCommandInvalid) {
            statusMsg = invalidCommandMessage;
            reportMsg = morningMsg = "";
        }

        if (this.isGameOver) {
            statusMsg = gameOverMessage;
            morningMsg = "";
        }

        return String.join("", statusMsg, reportMsg, morningMsg);
    }
}
