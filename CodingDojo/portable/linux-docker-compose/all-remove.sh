#!/usr/bin/env bash

cd ./applications
bash force-remove-all-images.sh
cd ..

rm -rf ./applications
rm -rf ./config
rm -rf ./logs
rm -rf ./ssl-cert
rm ./*