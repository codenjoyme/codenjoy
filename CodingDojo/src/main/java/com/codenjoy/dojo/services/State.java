package com.codenjoy.dojo.services;

/**
 * Created by Sanja on 25.10.2014.
 */
public interface State<E, T> {
    E state(T player, Object... alsoAtPoint);
}
