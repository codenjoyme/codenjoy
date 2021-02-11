def fromVersion = '1.1.1'
def toVersion = '1.1.2'

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

// collect all pom.xml and README.md files
def files = dirs3.collect {
    new File("$it/pom.xml")
} + dirs3.collect {
    new File("$it/README.md")
}
files << new File("${base}games\\engine\\setup.bat")
files << new File("${base}games\\engine\\setup.sh")
files << new File("${base}portable\\windows-cmd\\00-settings.bat")
files << new File("${base}portable\\linux-docker-compose\\balancer.yml")
files << new File("${base}portable\\linux-docker-compose\\codenjoy.yml")
files << new File("${base}portable\\linux-docker-compose\\rebuild.sh")
files << new File("${base}portable\\linux-docker\\2-build.sh")
files << new File("${base}portable\\linux-docker\\3-start.sh")
files << new File("${base}portable\\linux-docker\\run.sh")
files << new File("${base}games\\pom.xml")
files << new File("${base}games\\README.md")
files << new File("${base}pom.xml")
files << new File("${base}README.md")

// replace in all files
files.each {
    if (!it.exists()) return
    println it
    def text = it.text
    for (i in 1..5) {
        text = (text =~ /$fromVersion/).replaceFirst("$toVersion")
    }
    it.write(text)
}

