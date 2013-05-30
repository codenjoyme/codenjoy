Getting started:

1. Install Maven from http://maven.apache.org

2. Import Maven project from IDE

2.1. For IntelliJ IDEA
- Select from menu File->New Project...
- Import Project from external model, select Maven

2.2. For Eclipse
- Select from menu File->Import
- From Import window select Maven->Existing Maven Projects

3. Run with default command
	mvn clean jetty:run

4. Open in browser to see server in action
 http://localhost:8888/

5. Optional. Additional server options you might want to use with mvn command
-Djetty.port=8889 (default is 8888)
	 you might want to change this for running more that one bot in one machine)
-Djetty.refresh.interval=1 (default is 5)
	server automaticaly redeploys bot app every few seconds. Set to 0 to turn redeploy off
-Djetty.reload=manual (default is 'automatic') 
 	used in conjunction with a non-zero jetty.refresh.interval causes automatic hot redeploy when changes are detected. 
	Set to "manual" instead to trigger scanning by pressing Enter in console. 
	This might be useful when you are doing a series of changes that you want to ignore until you're done. 

