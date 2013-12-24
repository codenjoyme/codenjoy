package com.codenjoy.dojo.services;

/**
 * Еще обин архитектурный бок. Фреймворк отсчитывает тики для всех игор пользователей.
 * Но если игра многопользовательская (все на одной доске), тогда N тиков
 * игор юзеров {@see com.codenjoy.dojo.sample.model.SingleSample} приведут к N тикам одной борды {@@see com.codenjoy.dojo.sample.model.Sample}.
 * Вот чтобы этого не случилось в этом тикере и реализуется задержка.
 * TODO Когда-нибудь я это пофикшу
 */
public class Ticker {

    private int timer;
    private Players players;

    public Ticker(Players players) {
        this.players = players;
    }

    public boolean collectTicks() {
        timer++;
        if (timer >= players.getCount()) {
            timer = 0;
        } else {
            return true;
        }
        return false;
    }
}
