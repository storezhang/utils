#!/usr/bin/env bash

set -e

# 部署到Maven中央仓库
if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
    mvn --settings .travis/settings.xml -Psonatype-oss-release

    # 清理GPG
    if [ ! -z "$TRAVIS" -a -f "$HOME/.gnupg" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi

    source .travis/create_gpg.sh

    mvn clean deploy --settings .travis/settings.xml -DskipTests=true --batch-mode -Psonatype-oss-release

    # 清理GPG
    if [ ! -z "$TRAVIS" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi
fi