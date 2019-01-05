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
﻿namespace Loderunner.Api
{
    public enum BoardElement : short
    {
        None = (short)' ',

        Brick = (short)'#',
        PitFill1 = (short)'1',
        PitFill2 = (short)'2',
        PitFill3 = (short)'3',
        PitFill4 = (short)'4',
        UndestroyableWall = (short)'☼',

        DrillPit = (short)'*',

        EnemyLadder = (short)'Q',
        EnemyLeft = (short)'«',
        EnemyRight = (short)'»',
        EnemyPipeLeft = (short)'<',
        EnemyPipeRight = (short)'>',
        EnemyPit = (short)'X',

        Gold = (short)'$',

        HeroDie = (short)'Ѡ',
        HeroDrillLeft = (short)'Я',
        HeroDrillRight = (short)'R',
        HeroLadder = (short)'Y',
        HeroLeft = (short)'◄',
        HeroRight = (short)'►',
        HeroFallLeft = (short)']',
        HeroFallRight = (short)'[',
        HeroPipeLeft = (short)'{',
        HeroPipeRight = (short)'}',

        OtherHeroDie = (short)'Z',
        OtherHeroLeft = (short)')',
        OtherHeroRight = (short)'(',
        OtherHeroLadder = (short)'U',
        OtherHeroPipeLeft = (short)'Э',
        OtherHeroPipeRight = (short)'Є',

        Ladder = (short)'H',
        Pipe = (short)'~'
    }
}
