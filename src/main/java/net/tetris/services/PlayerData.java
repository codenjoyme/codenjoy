package net.tetris.services;

import java.util.List;

class PlayerData {
    PlayerData(List<Plot> plots, int score) {
        this.plots = plots;
        this.score = score;
    }

    private List<Plot> plots;
    private int score;

    public List<Plot> getPlots() {
        return plots;
    }

    public int getScore() {
        return score;
    }
}
