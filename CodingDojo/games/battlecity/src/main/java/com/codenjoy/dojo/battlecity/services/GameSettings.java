package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.Parameter;

public interface GameSettings {

    Dice getDice();

    Parameter<Integer> spawnAiPrize();

    Parameter<Integer> hitKillsAiPrize();

    Parameter<Integer> killYourTankPenalty();

    Parameter<Integer> killOtherHeroTankScore();

    Parameter<Integer> killOtherAITankScore();

    Parameter<Integer> prizeOnField();
}
