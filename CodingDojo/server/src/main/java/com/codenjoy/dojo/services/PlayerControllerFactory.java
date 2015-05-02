package com.codenjoy.dojo.services;

public interface PlayerControllerFactory {
    PlayerController get(Protocol protocol);
}
