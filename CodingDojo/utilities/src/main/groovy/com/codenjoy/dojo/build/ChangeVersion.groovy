def fromVersion = '1.1.0'
def toVersion = '1.1.1'

// select root directories
def base = '..\\..\\..\\..\\..\\..\\..\\..\\';
def dirs = []
new File(base).eachDir() { dir ->
    dirs << dir.path
}

// extract games directory
def dirs2 = []
dirs.each {
    if (it.contains('games')) {
        new File(it).eachDir {
            dirs2 << it.path
        }
    } else {
        dirs2 << it
    }
}

// work only with folder which contains pom.xml
def dirs3 = dirs2.findAll {
    def found = false
    new File(it).eachFile() {
        found |= it.name.contains('pom.xml');
    }
    found
}

// collect all pom.xml files
def files = dirs3.collect {
    new File("$it/pom.xml")
}
files << new File("${base}games\\engine\\setup.bat")
files << new File("${base}portable\\windows-cmd\\00-settings.bat")
files << new File("${base}portable\\linux-docker-compose\\balancer.yml")
files << new File("${base}portable\\linux-docker-compose\\codenjoy.yml")
files << new File("${base}portable\\linux-docker-compose\\rebuild.sh")
files << new File("${base}portable\\linux-docker\\start.sh")
files << new File("${base}games\\pom.xml")
files << new File("${base}pom.xml")

// replace in all files
files.each {
    println it
    def text = it.text
    for (i in 1..5) {
        text = (text =~ /$fromVersion/).replaceFirst("$toVersion")
    }
    it.write(text)
}

