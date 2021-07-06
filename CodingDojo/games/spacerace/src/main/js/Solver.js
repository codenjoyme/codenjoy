/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

var Solver =function (Direction, Element, logger) {

    return {

        /**
         * @return next hero action
         */
        get: function (board) {
            // TODO your code here
            
            // board examples
            logger.log("Board size: " + board.size)
            var allExtend = board.getAllExtended();
            var otherHerroes = allExtend.filter(function(extendedpoint){return extendedpoint.getElement() == Element.OTHER_HERO});
            var otherHeroesString =otherHerroes.map(function(extendedpoint){return "{X:" + extendedpoint.getX() + ", Y:" + extendedpoint.getY() + "}"}).join(", ");
            logger.log("Other heroes on coordinates: " + otherHeroesString);

            var bombsAndStones = board.findAll(Element.BOMB, Element.STONE);
            logger.log("Bombs and stones number: " + bombsAndStones.length);

            me = board.findAll(Element.HERO);
            if(me.length > 0){
                logger.log("Me on coordinates: {X:" + me[0].getX() + ", Y:" + me[0].getY() + "}");
            }
            var meIfMoveLeft = me[0].createAt(Direction.LEFT); // point left to the hero
 

            // direction examples
            var directions = [Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN];
            var direction = directions[randomInt(0, directions.length -1)];

            if(Math.random() > 0.5) direction = direction.withAct();

            return direction;
        }
    };
};


function randomInt(min, max){
    return Math.round(Math.random() * (min+max) - min);
}


if (module) module.exports = Solver;
