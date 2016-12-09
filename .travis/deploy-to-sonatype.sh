#!/usr/bin/env bash

set -e

# 只在检测到有新的标签的时候才部署
if [ ! -z "$TRAVIS_TAG" ]
then
    echo "发现了新的标签：$TRAVIS_TAG"
    mvn --settings .travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.3:set -DnewVersion=$TRAVIS_TAG -Prelease

    # 清理GPG
    if [ ! -z "$TRAVIS" -a -f "$HOME/.gnupg" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi

    source .travis/create_gpg.sh

    mvn clean deploy --settings .travis/settings.xml -DskipTests=true --batch-mode --update-snapshots -Prelease

    # 清理GPG
    if [ ! -z "$TRAVIS" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi
else
    echo "没有发现新Tag，继续保持Maven中央库的版"
fi