package com.codenjoy.dojo.bomberman.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import org.json.JSONObject;

public class OptionGameSettings implements GameSettings {

    private final Dice dice;

    private final Parameter<Boolean> isMultiple;
    private final Parameter<Integer> playersPerRoom;

    private final RoundSettingsWrapper roundSettings;

    private final Parameter<Boolean> isBigBadaboom;
    private final Parameter<Integer> bombPower;
    private final Parameter<Integer> bombsCount;
    private final Parameter<Integer> destroyWallCount;
    private final Parameter<Integer> boardSize;
    private final Parameter<Integer> meatChoppersCount;

    private final Parameter<Integer> killWallScore;
    private final Parameter<Integer> killMeatChopperScore;
    private final Parameter<Integer> killOtherHeroScore;
    private final Parameter<Integer> diePenalty;
    private final Parameter<Integer> catchPerkScore;
    private final Parameter<Integer> winRoundScore;

    private final Parameter<Integer> perkDropRatio;
    private final Parameter<Integer> perkPickTimeout;
    private final Parameter<Integer> perkBombBlastRadiusInc;
    private final Parameter<Integer> timeoutBombBlastRadiusInc;
    private final Parameter<Integer> perkBombCountInc;
    private final Parameter<Integer> timeoutBombCountInc;
    private final Parameter<Integer> timeoutBombImmune;
    private final Parameter<Integer> remoteControlCount;

    public OptionGameSettings(Settings settings, Dice dice) {
        this.dice = dice;

        isMultiple = settings.addCheckBox("[Game] Is multiple or disposable").type(Boolean.class).def(false);
        playersPerRoom = settings.addEditBox("[Game] Players per room for disposable").type(Integer.class).def(5);

        killWallScore = settings.addEditBox("[Score] Kill wall score").type(Integer.class).def(1);
        killMeatChopperScore = settings.addEditBox("[Score] Kill meat chopper score").type(Integer.class).def(10);
        killOtherHeroScore = settings.addEditBox("[Score] Kill other hero score").type(Integer.class).def(20);
        catchPerkScore = settings.addEditBox("[Score] Catch perk score").type(Integer.class).def(5);
        diePenalty = settings.addEditBox("[Score] Your hero's death penalty").type(Integer.class).def(30);
        winRoundScore = settings.addEditBox("[Score][Rounds] Win round score").type(Integer.class).def(30);

        isBigBadaboom = settings.addCheckBox("[Level] Blast activate bomb").type(Boolean.class).def(false);
        bombsCount = settings.addEditBox("[Level] Bombs count").type(Integer.class).def(1);
        bombPower = settings.addEditBox("[Level] Bomb power").type(Integer.class).def(3);
        boardSize = settings.addEditBox("[Level] Board size").type(Integer.class).def(23);
        destroyWallCount = settings.addEditBox("[Level] Destroy wall count").type(Integer.class).def(boardSize.getValue() * boardSize.getValue() / 10);
        meatChoppersCount = settings.addEditBox("[Level] Meat choppers count").type(Integer.class).def(5);

        roundSettings = new RoundSettingsWrapper(settings,
                true,  // roundsEnabled   - включен ли режим раундов
                200,   // timePerRound    - сколько тиков на 1 раунд
                1,     // timeForWinner   - сколько тиков победитель будет сам оставаться после всех побежденных
                5,     // timeBeforeStart - обратный отсчет перед началом раунда
                1,     // roundsPerMatch  - сколько раундов (с тем же составом героев) на 1 матч
                1);    // minTicksForWin  - сколько тиков должно пройти от начала раунда, чтобы засчитать победу

        int perksTimeout = 30;
        // perks. Set value to 0 = perk is disabled.
        perkDropRatio = settings.addEditBox("[Perks] Perks drop ratio in %").type(Integer.class).def(20); // 20%
        perkPickTimeout = settings.addEditBox("[Perks] Perks pick timeout").type(Integer.class).def(30);
        //Bomb blast radius increase (BBRI)
        perkBombBlastRadiusInc = settings.addEditBox("[Perks] Bomb blast radius increase").type(Integer.class).def(2);
        timeoutBombBlastRadiusInc = settings.addEditBox("[Perks] Bomb blast radius increase effect timeout").type(Integer.class).def(perksTimeout);
        // Bomb count increase (BCI)
        perkBombCountInc = settings.addEditBox("[Perks] Bomb count increase").type(Integer.class).def(4);
        timeoutBombCountInc = settings.addEditBox("[Perks] Bomb count effect timeout").type(Integer.class).def(perksTimeout);
        // Bomb immune (BI)
        timeoutBombImmune = settings.addEditBox("[Perks] Bomb immune effect timeout").type(Integer.class).def(perksTimeout);
        // Bomb remote control (BRC)
        remoteControlCount = settings.addEditBox("[Perks] Number of Bomb remote controls (how many times player can use it)").type(Integer.class).def(3);
    }

    @Override
    public Dice getDice() {
        return dice;
    }

    @Override
    public Level getLevel() {
        return new Level() {
            @Override
            public int bombsCount() {
                return bombsCount.getValue();
            }

            @Override
            public int bombsPower() {
                return bombPower.getValue();
            }

            @Override
            public int perksDropRate() {
                return perkDropRatio.getValue();
            }
        };
    }

    @Override
    public Walls getWalls() {
        OriginalWalls originalWalls = new OriginalWalls(boardSize);
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, meatChoppersCount, dice);

        return new EatSpaceWalls(meatChoppers, destroyWallCount, dice);
    }

    @Override
    public Hero getHero(Level level) {
        PerksSettingsWrapper.clear();
        PerksSettingsWrapper.setDropRatio(perkDropRatio.getValue());
        PerksSettingsWrapper.setPickTimeout(perkPickTimeout.getValue());

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE,
                perkBombBlastRadiusInc.getValue(), timeoutBombBlastRadiusInc.getValue());

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_COUNT_INCREASE,
                perkBombCountInc.getValue(), timeoutBombCountInc.getValue());

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_IMMUNE,
                0, timeoutBombImmune.getValue());

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_REMOTE_CONTROL,
                remoteControlCount.getValue(), 1); // 1 потому что он никогда не декризится

        return new Hero(level, dice);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }

    @Override
    public Parameter<Boolean> isMultiple() {
        return isMultiple;
    }

    @Override
    public Parameter<Boolean> isBigBadaboom() {
        return isBigBadaboom;
    }

    @Override
    public Parameter<Integer> getPlayersPerRoom() {
        return playersPerRoom;
    }

    @Override
    public RoundSettingsWrapper getRoundSettings() {
        return roundSettings;
    }

    @Override
    public Parameter<Integer> diePenalty() {
        return diePenalty;
    }

    @Override
    public Parameter<Integer> killOtherHeroScore() {
        return killOtherHeroScore;
    }

    @Override
    public Parameter<Integer> killMeatChopperScore() {
        return killMeatChopperScore;
    }

    @Override
    public Parameter<Integer> killWallScore() {
        return killWallScore;
    }

    @Override
    public Parameter<Integer> catchPerkScore() {
        return catchPerkScore;
    }

    @Override
    public Parameter<Integer> winRoundScore() {
        return winRoundScore;
    }

    @Override
    public Parameter<Integer> getDestroyWallCount() {
        return destroyWallCount;
    }

    @Override
    public Parameter<Integer> getBombPower() {
        return bombPower;
    }

    @Override
    public Parameter<Integer> getBombsCount() {
        return bombsCount;
    }

    @Override
    public Parameter<Integer> getMeatChoppersCount() {
        return meatChoppersCount;
    }

    public OptionGameSettings update(JSONObject json) {
        String name = "roundSettings";
        if (json.has(name)) {
            SettingsUtils.save(json.getJSONObject(name), roundSettings);
        }
        SettingsUtils.save(json, this);
        return this;
    }

    public JSONObject asJson() {
        JSONObject result = new JSONObject();
        JSONObject round = new JSONObject();
        result.put("roundSettings", round);
        SettingsUtils.load(round, roundSettings);
        SettingsUtils.load(result, this);
        return result;
    }
}
