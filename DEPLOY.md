# 发布命令
  - clean deploy -P sonatype-oss-release -Dgpg.passphrase=密码
  - gpg --list-keys
  - gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys 生成的密码短语
  - https://oss.sonatype.org/#stagingRepositories

# 参考
https://www.iteblog.com/archives/1807