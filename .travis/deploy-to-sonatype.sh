#!/usr/bin/env bash

set -e

# 只在检测到有新的标签的时候才部署
if [ ! -z "$TRAVIS_TAG" ]; then
    echo -e "发现新标签，准备将新版本发布到Maven中央仓库"

    # 清理GPG
    if [ ! -z "$TRAVIS" -a -f "$HOME/.gnupg" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi

    source .travis/create_gpg.sh

    mvn clean deploy --settings .travis/settings.xml -DskipTests=true --batch-mode --update-snapshots -Psonatype-oss-release

    # 清理GPG
    if [ ! -z "$TRAVIS" ]; then
        shred -v ~/.gnupg/*
        rm -rf ~/.gnupg
    fi
else
    echo -e "没有发现新标签，退出执行"
fi