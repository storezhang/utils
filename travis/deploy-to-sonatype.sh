#!/bin/bash

# 部署到Maven中央仓库
if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then


    mvn clean deploy --settings travis/settings.xml


    echo -e "成功部署${TRAVIS_JOB_NUMBER}到Maven中央仓库！\n"
fi