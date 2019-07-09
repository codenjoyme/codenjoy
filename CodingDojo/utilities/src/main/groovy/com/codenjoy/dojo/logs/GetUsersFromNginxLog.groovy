package main.groovy.com.codenjoy.dojo.logs

import org.json.JSONObject

import java.util.regex.Matcher

def base = 'D:\\Work\\2019-07-09\\EPAM\\Парсил логи LearningWeek чтобы достать юзеров\\nginx';

// get all files
def files = []
new File(base).eachFile { file ->
    files << new File(file.path)
}

// filter and parse lines
def logs = []
def actions = [:]
Matcher mather = "subject" =~ /regex/
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
            def suffix = '\u0026code='
            def playerId = log.substring(
                    log.indexOf(prefix) + prefix.length(),
                    log.indexOf(suffix))

            println time + ' > ' + playerId
        }
    }
}

