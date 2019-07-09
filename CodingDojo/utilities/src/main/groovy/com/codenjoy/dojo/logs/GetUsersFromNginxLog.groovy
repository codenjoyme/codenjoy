package main.groovy.com.codenjoy.dojo.logs

import org.json.JSONObject

def base = 'D:\\Work\\2019-07-09\\EPAM\\Парсил логи LearningWeek чтобы достать юзеров\\nginx';

// get all files
def files = []
new File(base).eachFile { file ->
    files << new File(file.path)
}

// filter and parse lines
def logs = []
files.each {
    println it
    def text = it.text
    text.split('\n').each { line ->
        def json = new JSONObject(line)
        if (!'stderr'.equals(json.getString('stream'))
                && json.has('log')
                && !json.getString('log').contains('ws?user=0&code=000000000000')
                && json.getString('log').contains('GET /codenjoy-contest/ws?user='))
        {
            logs << json
            println json.toString()
        }
    }
}

