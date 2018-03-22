### 如何使用

- git clone git@github.com:Yaccc/leafserver.git &&
cd leafserver
- 在根目录下找到coreTable.sql这张表,在你的MySQL中创建一个名叫leaf的数据库,其中创建这张表
- 在leafserver/leafserver-starter/src/main/profile/目录下找到每个环境的yaml配置文件
默认环境是dev模式,修改yaml中的数据库url和账号和密码等
- 在 leafserver 目录下执行 mvn clean package -Pdev(表示打包dev环境的yaml文件)
- 在 leafserver/leafserver-starter/target 目录下会生成一个可执行的jar包
- exec `java -jar leafserver-1.0-SNAPSHOP.jar` 运行项目 看到`Started leafserver , enjoy it!!` 表示你成功运行了
- 执行`curl http://localhost:9999/id?app=${app}&key=${key}` 命令得到一个数据该app和对应key的ID号码



