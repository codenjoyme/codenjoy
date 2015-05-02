package com.codenjoy.dojo.services;

public interface State<E, T> {
    E state(T player, Object... alsoAtPoint);
}
