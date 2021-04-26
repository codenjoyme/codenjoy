python -m venv --system-site-packages .\.dojo
cmd /k ".\.dojo\scripts\activate.bat & pip install six websocket-client & python .\tests_run_tests_suite.py"


