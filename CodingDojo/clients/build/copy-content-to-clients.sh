#!/usr/bin/env bash

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

BLUE=94
GRAY=89

eval_echo() {
    command=$1
    if [[ "$2" == "" ]]; then
        color=$BLUE
    else
        color=$2
    fi
    echo "[${color}m$command[0m"
    echo
    eval $command
}

color() {
    message=$1
    echo "[93m$message[0m"
}

install_all() {
   from=content/$1
   to=$2
   eval_echo "cp $from ../cpp/$to" $GRAY
   eval_echo "cp $from ../csharp/$to" $GRAY
   eval_echo "cp $from ../go/$to" $GRAY
   eval_echo "cp $from ../java/$to" $GRAY
   eval_echo "cp $from ../java-script/$to" $GRAY
   eval_echo "cp $from ../kotlin/$to" $GRAY
   eval_echo "cp $from ../php/$to" $GRAY
   eval_echo "cp $from ../pseudo/$to" $GRAY
   eval_echo "cp $from ../python/$to" $GRAY
   eval_echo "cp $from ../ruby/$to" $GRAY
   eval_echo "cp $from ../scala/$to" $GRAY
}

install_jvm() {
   from=content/$1
   to=$2
   eval_echo "cp $from ../java/$to" $GRAY
   eval_echo "cp $from ../kotlin/$to" $GRAY
   eval_echo "cp $from ../pseudo/$to" $GRAY
   eval_echo "cp $from ../scala/$to" $GRAY
}

eval_echo "install_all 'run.bat' 'build/run.bat'"
eval_echo "install_all 'run.sh' 'build/run.sh'"
# eval_echo "install_all '.env' '.env'"
eval_echo "install_jvm 'jvm_stuff.bat' 'build/stuff.bat'"

echo
color "Press Enter to continue"
read