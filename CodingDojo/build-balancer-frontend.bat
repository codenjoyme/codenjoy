set ROOT=%CD%

cd %ROOT%\balancer-frontend
call npm i
call npm audit fix

cd %ROOT%

pause >nul