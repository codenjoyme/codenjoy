package com.codenjoy.dojo.snakebattle.services;

import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.snakebattle.model.level.Level;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings>, RoundSettings {

    public enum Keys implements Key {

        PLAYERS_PER_ROOM("Players per Room"),
        FLYING_COUNT("Flying count"),
        FURY_COUNT("Fury count"),
        STONE_REDUCED("Stone reduced value"),
        WIN_SCORE("Win score"),
        APPLE_SCORE("Apple score"),
        GOLD_SCORE("Gold score"),
        DIE_PENALTY("Die penalty"),
        STONE_SCORE("Stone score"),
        EAT_SCORE("Eat enemy score"),
        LEVEL_MAP("Level map");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }
    
    public GameSettings() {
        addCheckBox(ROUNDS_ENABLED.key()).type(Boolean.class).def(true);
        addEditBox(TIME_PER_ROUND.key()).type(Integer.class).def(300);
        addEditBox(TIME_FOR_WINNER.key()).type(Integer.class).def(1);
        addEditBox(TIME_BEFORE_START.key()).type(Integer.class).def(5);
        addEditBox(ROUNDS_PER_MATCH.key()).type(Integer.class).def(3);
        addEditBox(MIN_TICKS_FOR_WIN.key()).type(Integer.class).def(40);

        addEditBox(PLAYERS_PER_ROOM.key()).type(Integer.class).def(5);
        addEditBox(FLYING_COUNT.key()).type(Integer.class).def(10);
        addEditBox(FURY_COUNT.key()).type(Integer.class).def(10);
        addEditBox(STONE_REDUCED.key()).type(Integer.class).def(3);

        addEditBox(WIN_SCORE.key()).type(Integer.class).def(50);
        addEditBox(APPLE_SCORE.key()).type(Integer.class).def(1);
        addEditBox(GOLD_SCORE.key()).type(Integer.class).def(10);
        addEditBox(DIE_PENALTY.key()).type(Integer.class).def(0);
        addEditBox(STONE_SCORE.key()).type(Integer.class).def(5);
        addEditBox(EAT_SCORE.key()).type(Integer.class).def(10);

        addEditBox(LEVEL_MAP.key()).type(String.class).multiline().def(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼         ○                 ☼" +
                "☼#                           ☼" +
                "☼☼  ○   ☼#         ○         ☼" +
                "☼☼                      ○    ☼" +
                "☼# ○         ●               ☼" +
                "☼☼                ☼#        %☼" +
                "☼☼      ☼☼☼        ☼  ☼      ☼" +
                "☼#      ☼      ○   ☼  ☼      ☼" +
                "☼☼      ☼○         ☼  ☼      ☼" +
                "☼☼      ☼☼☼               ●  ☼" +
                "☼#              ☼#           ☼" +
                "☼☼○                         $☼" +
                "☼☼    ●              ☼       ☼" +
                "☼#             ○             ☼" +
                "☼☼                           ☼" +
                "☼☼   ○             ☼#        ☼" +
                "☼#       ☼☼ ☼                ☼" +
                "☼☼          ☼     ●     ○    ☼" +
                "☼☼       ☼☼ ☼                ☼" +
                "☼#          ☼               @☼" +
                "☼☼         ☼#                ☼" +
                "☼☼           ○               ☼" +
                "☼#                  ☼☼☼      ☼" +
                "☼☼                           ☼" +
                "☼☼      ○        ☼☼☼#    ○   ☼" +
                "☼#                           ☼" +
                "☼☼     ╘►        ○           ☼" +
                "☼☼                           ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
