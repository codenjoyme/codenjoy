package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 3:37 PM
 */
public interface PlayerScores extends EventListener {

    int getScore();

    int clear();
}
