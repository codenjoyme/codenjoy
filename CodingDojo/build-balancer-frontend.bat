set ROOT=%CD%

cd %ROOT%\balancer-frontend
call npm i
call npm npm audit fix

pause >nul