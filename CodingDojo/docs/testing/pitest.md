Pitest (aka PIT) is a state of the art mutation testing 
system for Java and the JVM. We use pitest as a 
testing tool that allows you to learn test 
coverage by mutating the source code.

Pitest is used as a maven plugin that is tied to the 
`test` phase and performs a `mutationCoverage` goal.

The result of pitest processing can be found 
under `${project.basedir}/pitest/` output folder.

Because of performance this option is disabled by default. 
To run it please use command on root project

`mvn clean install --fail-at-end -Pmutation`

or any child project

`mvn clean install -Pmutation`