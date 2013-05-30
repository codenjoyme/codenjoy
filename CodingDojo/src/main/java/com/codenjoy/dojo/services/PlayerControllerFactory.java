package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 16:32
 */
public interface PlayerControllerFactory {

    PlayerController get(Protocol protocol);
}
