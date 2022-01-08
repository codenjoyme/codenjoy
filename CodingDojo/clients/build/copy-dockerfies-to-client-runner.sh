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

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

color() {
    message=$1
    echo "[93m$message[0m"
}

copy_dockerfiles() {
    from_lng=$1
    to_lng=$1
    to_dir=../../client-runner/src/main/resources/dockerfiles
    if [[ "$from_lng" == "java-script" ]]; then
        to_lng=javascript
    fi
    eval_echo "cp ../$from_lng/Dockerfile $to_dir/$to_lng/"
}

eval_echo "copy_dockerfiles 'csharp'"
eval_echo "copy_dockerfiles 'go'"
eval_echo "copy_dockerfiles 'java'"
eval_echo "copy_dockerfiles 'java-script'"
eval_echo "copy_dockerfiles 'kotlin'"
eval_echo "copy_dockerfiles 'php'"
eval_echo "copy_dockerfiles 'pseudo'"
eval_echo "copy_dockerfiles 'python'"
eval_echo "copy_dockerfiles 'ruby'"
eval_echo "copy_dockerfiles 'scala'"

echo
color "Press Enter to continue"
read