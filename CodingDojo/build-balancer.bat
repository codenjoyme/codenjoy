set ROOT=%CD%

cd %ROOT%\balancer
call %ROOT%\mvnw clean package -DskipTests

cd %ROOT%

pause >nul