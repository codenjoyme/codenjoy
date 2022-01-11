package com.codenjoy.dojo.logs

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import org.json.JSONObject

def base = '';

// get all files
def files = []
new File(base).eachFile { file ->
    files << new File(file.path)
}

// load users
def users = [:]
new File(base + '\\..\\database\\users.txt').text.split('\r\n').each {
    def data = it.split('\t')
    users[data[1]] = data[0]
}

// filter and parse lines
def logs = []
def actions = [:]
files.each {
    println it
    def text = it.text
    text.split('\n').each { line ->
        def json = new JSONObject(line)
        def log = json.getString('log')
        if (!'stderr'.equals(json.getString('stream'))
                && json.has('log')
                && !log.contains('ws?user=0&code=000000000000')
                && log.contains('GET /codenjoy-contest/ws?user='))
        {
            logs << json
            def time = json.getString('time')

            def prefix = '/ws?user='
            def suffix = '&code='
            def playerId = log.substring(
                    log.indexOf(prefix) + prefix.length(),
                    log.indexOf(suffix))

            def user = users[playerId]
            if (user == null) {
                user = "user('$playerId')"
            }
            if (['63gk89b3j87z87a83eri',
                    'zemm5yx98w7rde0697p6',
                    'hwrreckowfd255qe59b3'].contains(playerId))
            {
                return
            }
            if (actions[user] == null) {
                actions[user] = new TreeSet()
            }
            actions[user] << time

//            println time + ' > ' + user
        }
    }
}

println 'activity'
def sorted = [:]
actions.each{ user, times ->
    println "${user} : ${times.size()}"
}



