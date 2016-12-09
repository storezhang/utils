#!/usr/bin/env bash

set -e

# 创建一个随机的GPG短语
export GPG_PASSPHRASE=$(echo "$RANDOM$(date)" | md5sum | cut -d\  -f1)

# 配置生成GPG密钥
cat >gen-key-script <<EOF
    %echo -e 开始生成GPG
    Key-Type: RSA
    Key-Length: 2048
    Subkey-Type: 1
    Subkey-Length: 2048
    Name-Real: storezhang
    Name-Email: storezhang@gmail.com
    Expire-Date: 0
    Passphrase: ${GPG_PASSPHRASE}
    %commit
    %echo -e 生成GPG成功
EOF

# 创建GPG对
gpg --batch --gen-key gen-key-script

# 导出GPG对
export GPG_KEYNAME = $(gpg -K | grep ^sec | cut -d/  -f2 | cut -d\  -f1 | head -n1)
echo -e "生成的GPG是：（${GPG_KEYNAME}=${GPG_PASSPHRASE}）"

# 清除本地配置
shred gen-key-script

# 将本地GPG发布到Ubuntu的服务器上
gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys ${GPG_KEYNAME}

# 等待刚发布的GPG可用
while (true); do
  date
  gpg --keyserver hkp://keyserver.ubuntu.com:11371 --recv-keys ${GPG_KEYNAME} && break || sleep 30
done