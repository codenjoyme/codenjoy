package main.groovy.com.codenjoy.dojo.logs

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



