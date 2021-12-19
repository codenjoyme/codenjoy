#!/usr/bin/env bash

if [[ "$LANGUAGE" == "" ]]; then
    LANGUAGE=java
fi
cd ./../$LANGUAGE/build
bash run.sh

echo Press Enter to continue
pause >nul