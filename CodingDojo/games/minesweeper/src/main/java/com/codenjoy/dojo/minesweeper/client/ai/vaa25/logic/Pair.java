package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;

/**
 * Created by vaa25 on 15.03.2015.
 */
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
