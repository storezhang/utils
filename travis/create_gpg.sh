#!/usr/bin/env bash

set -e

# 创建一个随机的GPG短语
export GPG_PASSPHRASE=$(echo "$RANDOM$(date)" | md5sum | cut -d\  -f1)

# configuration to generate gpg keys
cat >gen-key-script <<EOF
    %echo Generating a basic OpenPGP key
    Key-Type: RSA
    Key-Length: 4096
    Subkey-Type: 1
    Subkey-Length: 4096
    Name-Real: Opensource Idealo
    Name-Email: opensource-logback-redis@idealo.de
    Expire-Date: 2y
    Passphrase: ${GPG_PASSPHRASE}
    %commit
    %echo done
EOF

# 创建GPG对
gpg --batch --gen-key gen-key-script


# 导出GPG对
export GPG_KEYNAME=$(gpg -K | grep ^sec | cut -d/  -f2 | cut -d\  -f1 | head -n1)

# 清除本地配置
shred gen-key-script

# 将本地GPG发布到Ubuntu的服务器上
gpg --keyserver keyserver.ubuntu.com --send-keys ${GPG_KEYNAME}

# 等待刚发布的GPG可用
while (true); do
  date
  gpg --keyserver keyserver.ubuntu.com  --recv-keys ${GPG_KEYNAME} && break || sleep 30
done