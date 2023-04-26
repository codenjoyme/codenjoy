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

read -p "Enter the number of the port you want to free: " port
pids=$(netstat -tlnp | grep :$port | awk '{print $7}' | cut -d/ -f1 | sort -u)
echo "Process to kill: [$pids]"
for pid in $pids 
do
    echo "Terminating a process with PID $pid"
    kill -9 $pid
done
echo "The process using the $port port is terminated."
read
