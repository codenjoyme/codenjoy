package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;

public class PlayerControllerFactoryImpl implements PlayerControllerFactory {

    @Autowired
    protected PlayerController httpPlayerController;

    @Autowired
    protected PlayerController wsPlayerController;

    @Override
    public PlayerController get(Protocol protocol) {
        if (Protocol.WS.equals(protocol)){
            return wsPlayerController;
        }
        return httpPlayerController;
    }
}
