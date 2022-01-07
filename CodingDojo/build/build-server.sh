#!/bin/bash

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2012 - 2022 Codenjoy
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
# #L%
###

cd ..

export ROOT=$PWD

if [ "$GAMES_TO_RUN" == "" ] ; then
	# export GAMES_TO_RUN=all
	export GAMES_TO_RUN=mollymage
	# export GAMES_TO_RUN=tetris,knibert,mollymage
fi

echo Building server with [$GAMES_TO_RUN]

if [ "$GAMES_TO_RUN" == "all" ] ; then
    "$ROOT/mvnw" clean install -DskipTests
    
    cd "$ROOT/server" || return
    "$ROOT/mvnw" clean package -DskipTests -DallGames
else
    cd "$ROOT/games" || return
    "$ROOT/mvnw" clean install -N

    cd "$ROOT/clients/java" || return
    "$ROOT/mvnw" clean install -DskipTests

    cd "$ROOT/games/engine" || return
    "$ROOT/mvnw" clean install -DskipTests
    
    for GAME in ${GAMES_TO_RUN//" "/"",""} ; do
        cd "$ROOT/games/$GAME" || return
        "$ROOT/mvnw" clean install -DskipTests
    done
    
    cd "$ROOT/server" || return
    "$ROOT/mvnw" clean package -DskipTests -P$GAMES_TO_RUN
fi

cd "$ROOT" || return
