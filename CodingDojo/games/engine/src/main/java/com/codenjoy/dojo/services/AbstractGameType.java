package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * Класс позволяет не фиксить все игры, если будет добьавлен интерфейсный метод в GameType
 * Так же содержит наиболее общий код, актуальный для всех игр
 */
public abstract class AbstractGameType implements GameType {

    protected final Settings settings;

    public AbstractGameType() {
        settings = new SettingsImpl();
    }

    @Override
    public boolean newAI(String aiName) {
        // do nothing
        return false;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }


    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public String getVersion() {
        return VersionReader.getCurrentVersion(name());
    }

    /**
     * Этот метод будет дергаться после всех тиков всех игор
     */
    @Override
    public void tick() {
        // do nothing
    }

    /**
     * @return нормальный Random, но ты можешь переопределить его, например, для тестовых целей
     */
    protected Dice getDice() {
        return new RandomDice();
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return new PrinterFactoryImpl();
    }

}
