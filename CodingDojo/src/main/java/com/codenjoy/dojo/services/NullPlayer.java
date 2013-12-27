package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 6:49 AM
 */
public class NullPlayer extends Player {

    NullPlayer() {
       super(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
               PlayerScores.NULL, Information.NULL, Protocol.WS);
    }
}

