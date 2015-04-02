package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 16:33
 */
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
