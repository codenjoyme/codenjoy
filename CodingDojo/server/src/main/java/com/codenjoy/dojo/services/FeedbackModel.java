package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

public class FeedbackModel {
    private String fromPlayerId;
    private String forGame;
    private String feedbackText;

    public String getForGame() {
        return forGame;
    }

    public String getFromPlayerId() {
        return fromPlayerId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setForGame(String forGame) {
        this.forGame = forGame;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public void setFromPlayerId(String fromPlayerId) {
        this.fromPlayerId = fromPlayerId;
    }
}
