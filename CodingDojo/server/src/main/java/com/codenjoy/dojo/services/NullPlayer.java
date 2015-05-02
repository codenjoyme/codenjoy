package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

public class NullPlayer extends Player {

    public static final Player INSTANCE = new NullPlayer();

    private NullPlayer() {
       super(StringUtils.EMPTY, StringUtils.EMPTY, NullGameType.INSTANCE,
               NullPlayerScores.INSTANCE, NullInformation.INSTANCE, Protocol.WS);
    }
}

