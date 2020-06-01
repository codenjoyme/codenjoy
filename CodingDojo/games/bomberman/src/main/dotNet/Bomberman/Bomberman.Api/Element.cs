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
namespace Bomberman.Api
{
    public enum Element : short
    {
        /// <summary>
        /// This is your Bomberman. This is what he usually looks like :)
        /// </summary>
        BOMBERMAN = (short)'☺',          
   
        /// <summary>
        /// Your bomberman is sitting on own bomb
        /// </summary>
        BOMB_BOMBERMAN = (short)'☻',        

        /// <summary>
        /// Your dead Bomberman 
        /// </summary>
        /// <remarks>
        /// Don't worry, he will appear somewhere in next move. 
        /// You're getting -200 for each death
        /// </remarks>
        DEAD_BOMBERMAN = (short)'Ѡ',        

        /// <summary>
        /// This is other players alive Bomberman
        /// </summary>
        OTHER_BOMBERMAN = (short)'♥',

        /// <summary>
        /// This is other players Bomberman -  just set the bomb
        /// </summary>
        OTHER_BOMB_BOMBERMAN = (short)'♠',  

        /// <summary>
        /// Other players Bomberman's corpse 
        /// </summary>
        /// <remarks>
        /// It will disappear shortly, right on the next move. If you've done it you'll get score points
        /// </remarks>
        OTHER_DEAD_BOMBERMAN = (short)'♣',  

        /// <summary>
        /// Bomb with timer "5 tacts to boo-o-o-m!"
        /// </summary>
        /// <remarks>
        /// After bomberman set the bomb, the timer starts (5 ticks)
        /// </remarks>
        BOMB_TIMER_5 = (short)'5',          

        /// <summary>
        /// Bomb with timer "4 tacts to boom"
        /// </summary>
        BOMB_TIMER_4 = (short)'4',     

        /// <summary>
        /// Bomb with timer "3 tacts to boom"
        /// </summary>
        BOMB_TIMER_3 = (short)'3',

        /// <summary>
        /// Bomb with timer "2 tacts to boom"
        /// </summary>
        BOMB_TIMER_2 = (short)'2',
        
        /// <summary>
        /// Bomb with timer "1 tact to boom"
        /// </summary>
        BOMB_TIMER_1 = (short)'1',
        
        /// <summary>
        /// Boom! This is what is bomb does, everything that is destroyable got destroyed
        /// </summary>
        BOOM = (short)'҉',                  

        /// <summary>
        /// Wall that can't be destroyed.
        /// Indestructible wall will not fall from bomb.
        /// </summary>
        WALL = (short)'☼',                  

        /// <summary>
        /// Destroyable wall. It can be blowed up with a bomb (with score points)
        /// </summary>
        DESTROYABLE_WALL = (short)'#',          

        /// <summary>
        /// Walls ruins. This is how broken wall looks like, it will dissapear on next move.
        /// </summary>
        DestroyedWall = (short)'H',        

        /// <summary>
        /// Meat chopper. This guys runs over the board randomly and gets in the way all the time. If it will touch bomberman - bomberman dies.
        /// </summary>
        MEAT_CHOPPER = (short)'&',          

        /// <summary>
        /// Dead meat chopper. score point for killing.
        /// </summary>
        DeadMeatChopper = (short)'x',     

        /// <summary>
        /// Empty space on a map. This is the only place where you can move your Bomberman
        /// </summary>
        Space = (short)' ',

        ///<summary>
        /// Bomb blast radius increase. Applicable only to new bombs. The perk is temporary.
        ///</summary>
        BOMB_BLAST_RADIUS_INCREASE = (short)'+',

        ///<summary>
        /// Increase available bombs count. Number of extra bombs can be set in settings. Temporary.
        ///</summary>
        BOMB_COUNT_INCREASE = (short)'c',

        ///<summary>
        /// Bomb blast not by timer but by second act. Number of RC triggers is limited and can be set in settings.
        ///</summary>
        BOMB_REMOTE_CONTROL = (short)'r',

        ///<summary>
        /// Do not die after bomb blast (own bombs and others as well). Temporary.
        ///</summary>
        BOMB_IMMUNE = (short)'i'
    }
}
