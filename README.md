# leafserver
A high performance distributed unique ID generation system

### 中文文档

### how to start

- git clone git@github.com:Yaccc/leafserver.git &&
cd leafserver
- find coreTable.sql then create this table in database *leaf* on your MySQL
- in leafserver/leafserver-starter/src/main/profile/dev/application.yaml && modify url, username and password to your MySQL's
- cd leafserver && mvn clean package
- cd leafserver/leafserver-starter/target 
- exec `java -jar leafserver-1.0-SNAPSHOP.jar`
- `curl http://localhost:9999/id?app=${app}&key=${key}` to get one ID before added data(app & key) to your DB table






   


  
  
