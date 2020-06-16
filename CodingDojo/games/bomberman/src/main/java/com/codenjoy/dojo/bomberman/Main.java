package com.codenjoy.dojo.bomberman;

import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.bomberman.services.OptionGameSettings;
import com.codenjoy.dojo.client.local.ws.LocalWSGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SimpleParameter;

public class Main {

    public static void main(String[] args) {
        Dice dice = new RandomDice();

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected GameSettings getGameSettings() {
                return new OptionGameSettings(new SettingsImpl(), dice){
                    @Override
                    public Parameter<Boolean> isMultiple() {
                        return new SimpleParameter<>(true);
                    }

                    @Override
                    public RoundSettingsWrapper getRoundSettings() {
                        return new RoundSettingsWrapper(){
                            @Override
                            public Parameter<Boolean> roundsEnabled() {
                                return new SimpleParameter<>(false);
                            }
                        };
                    }

                    // pres Ctrl-O here and override any setting property
                };
            }
        };

        LocalWSGameRunner.run(gameType, "127.0.0.1", 8080);
    }
}
