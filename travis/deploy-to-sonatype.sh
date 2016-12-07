#!/bin/bash

# 部署到Maven中央仓库
if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

    # 清理GPG
    if [ ! -z "$TRAVIS" -a -f "$HOME/.gnupg" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi
    # 生成GPG
    source ./create_gpg.sh
    echo -e "成功生成GPG短语${evn.GPG_PASSPHRASE}！\n"

    mvn clean deploy -Psonatype-oss-release -DskipTests=true -Dgpg.skip=true --settings travis/settings.xml -Dgpg.passphrase=${env.GPG_PASSPHRASE}

    # 清理GPG
    if [ ! -z "$TRAVIS" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi

    echo -e "成功部署${TRAVIS_JOB_NUMBER}到Maven中央仓库！\n"
fi