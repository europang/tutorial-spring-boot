Spring Boot Tutorial, from the scratch
===================


First, Create New Project using STS, https://spring.io/tools/sts

**New > Spring Starter Project**

* Name : gsshop.jbp.dashboard
* Group : gsshop.boot
* Artifact : gsshop-jbp-dashboard
* Version : 0.0.1-SNAPSHOT
* Description : JBP Dashboard
* Package : gsshop.jbp.dashboard

**Next**

* Dependencies : Web (Checked)

**Finish**

And Then, Run Spring Boot Application

**Run As > Spring Boot App**

---
### Step 1.
Controllers and Static Resources

* API Test, http://localhost:8080/api/sample
* Static Resource Test, http://localhost:8080/sample-image.jpg

---
### Step 2-1.

JDBC Connection

Run Mysql, https://hub.docker.com/_/mysql/

```
MYSQL_CONTAINER_NAME="mysql"
MYSQL_DATABASE="jbp"
MYSQL_ROOT_PASSWORD="root-password"
MYSQL_USER="jbp-user"
MYSQL_PASSWORD="jbp-user-password"
docker run \
  --name ${MYSQL_CONTAINER_NAME}  -d \
  -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
  -e MYSQL_USER=${MYSQL_USER} \
  -e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
  -e MYSQL_DATABASE=${MYSQL_DATABASE} \
  -p 3306:3306 \
  mysql:5.7
```

create mysql sample table

```
docker exec -it mysql bash
# mysql -u jbp-user -p
mysql> use jbp
mysql> CREATE TABLE SAMPLE (
    id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id) );
mysql> INSERT INTO SAMPLE ( email, name ) VALUES ( 'yohany@gmail.com', 'John Kim' );
mysql> exit
# exit
```

Add dependencies into `pom.xml`

```
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>1.2.0</version>
</dependency>
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>6.0.5</version>
</dependency>
```

Add datasource configuration into `src/main/resources/application.properties`

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/jbp?autoReconnect=true&useSSL=false
spring.datasource.username=jbp-user
spring.datasource.password=jbp-user-password
```

Add SQL Mapper class `gsshop.jbp.dashboard.mapper.SampleMapper` and modify Controller `gsshop.jbp.dashboard.controller.DashboardAPIController`


* API Test, http://localhost:8080/api/sample/1

---
### Step 2-2.

replace Mapper Query Annotation with Query XML

* /src/main/java/gsshop/jbp/dashboard/mapper/SampleMapper.java
* /src/main/resources/application.properties
* /src/main/resources/mapper/SampleMapper.xml

---
### Step 3-1.

Create tables and insert datas.

```
docker exec -it mysql bash

# mysql -u jbp-user -p

mysql> use jbp

mysql> CREATE TABLE USER (
    username            VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    name                VARCHAR(255) NOT NULL,
    isAccountNonExpired BOOLEAN,
    isAccountNonLocked  BOOLEAN,
    isCredentialsNonExpired BOOLEAN,
    isEnabled           BOOLEAN,
    PRIMARY KEY (username) );

mysql> CREATE TABLE AUTHORITY (
    username      VARCHAR(255)  NOT NULL,
    authorityName VARCHAR(20)   NOT NULL );

mysql> INSERT INTO USER VALUES ('john', '1234', 'John Kim', true, true, true, true);
mysql> INSERT INTO USER VALUES ('ally', '1234', 'Ally Kim', true, true, true, true);
mysql> INSERT INTO AUTHORITY VALUES ('john', 'ADMIN');
mysql> INSERT INTO AUTHORITY VALUES ('john', 'USER' );
mysql> INSERT INTO AUTHORITY VALUES ('ally', 'USER' );

```

* /src/main/java/gsshop/jbp/dashboard/security/\*.java
* /src/main/resources/mapper/UserMapper.xml
* test code
