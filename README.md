# tutorial-spring-boot

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
### Step 2.
JDBC Connection

Run Mysql, https://hub.docker.com/_/mysql/

```
$ MYSQL_CONTAINER_NAME="mysql"
$ MYSQL_DATABASE="jbp"
$ MYSQL_ROOT_PASSWORD="root-password"
$ MYSQL_USER="jbp-user"
$ MYSQL_PASSWORD="jbp-user-password"
$ docker run \
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
$ docker exec -it mysql bash
# mysql -u jbp-user -p
mysql> use jbp
mysql> CREATE TABLE SAMPLE (
    id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id) );
mysql> exit
# exit
```

Add dependencies into pom.xml

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
