package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.InformationCollector;
import com.codenjoy.dojo.services.PlayerScores;

public class Info extends InformationCollector {

    public Info(PlayerScores playerScores) {
        super(playerScores);
    }

    @Override
    public void event(Object event) {
        pool.add(event.toString());
        super.event(event);
    }
}
