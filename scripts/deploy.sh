#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/api-0.0.1-SNAPSHOT.jar \
    kirilltobola@92.63.104.169:/home/kirilltobola/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa kirilltobola@92.63.104.169 << EOF
pgrep java | xargs kill -9
nohup java -jar api-0.0.1-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'