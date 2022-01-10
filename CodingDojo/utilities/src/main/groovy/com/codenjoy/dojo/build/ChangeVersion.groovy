package com.codenjoy.dojo.build

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


def fromVersion = properties['replace.from']
def toVersion = properties['replace.to']
if (fromVersion == null || toVersion == null) {
    println "Please set 'replace.from' and 'replace.to' env variables"
    return
}

// select root directories
def base = new File('.').canonicalPath.contains('src')
        ? '../../../../../../../../'
        : '../'

println "Processing folder: " + new File(base).canonicalPath
println "Replace '$fromVersion' to '$toVersion'"

def dirs = []
new File(base).eachDir() { dir ->
    dirs << dir.canonicalPath
}

// extract games directory
def dirs2 = []
process(dirs, 'games', dirs2)
process(dirs, 'clients', dirs2)

private List<Object> process(ArrayList dirs, folder, dirs2) {
    dirs.each {
        if (it.contains(folder)) {
            new File(it).eachDir {
                dirs2 << it.canonicalPath
            }
        } else {
            dirs2.contains(it) ? null : dirs2 << it
        }
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
files << new File("${base}games/engine/build/setup.bat")
files << new File("${base}games/engine/build/setup.sh")

files << new File("${base}portable/windows-cmd/00-settings.bat")

files << new File("${base}portable/linux-docker-compose/.env")
files << new File("${base}portable/linux-docker-compose/README.md")

files << new File("${base}portable/linux-docker/2-build.sh")
files << new File("${base}portable/linux-docker/3-start.sh")
files << new File("${base}portable/linux-docker/README.md")
files << new File("${base}portable/linux-docker/run.sh")

files << new File("${base}pom.xml")
files << new File("${base}README.md")

Collections.sort(files)

// replace in all files
files.each {
    if (!it.exists()) return
    println "Processing file: " + it.canonicalPath

    it.text = it.text.replace(fromVersion, toVersion)
}