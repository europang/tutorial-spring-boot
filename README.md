# Spring Boot Tutorial from the scratch.


**Spring Boot + Spring Security + MyBatis + Handlebars**

*GSSHOP, John Kim (yohany@gmail.com)*


## 0. Overview

> Reference site - https://projects.spring.io/spring-boot/

Spring Boot favors convention over configuration and is designed to get you up and running as quickly as possible.

Most Spring Boot applications need very little Spring configuration.

#### Features

- Create stand-alone Spring applications
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
- Provide opinionated 'starter' POMs to simplify your Maven configuration
- Automatically configure Spring whenever possible
- Provide production-ready features such as metrics, health checks and externalized configuration
- Absolutely **no code generation** and **no requirement for XML** configuration



## 1. Preparation

Download STS (Spring Tool Suite™) from  https://spring.io/tools/sts/all



## 2. Create Spring Project

> Reference site
>
> http://start.spring.io/
>
> https://docs.spring.io/spring-boot/docs/current/reference/html/index.html



**New > Spring Starter Project**

- Name : gsshop.web.jbp
- Group : gsshop.web
- Artifact : gsshop-web-jbp
- Version : 0.0.1-SNAPSHOT
- Description : JBP Dashboard
- Package : gsshop.web.jbp

**Next**

- Dependencies : Web (Checked)

**Finish**



And Then, Run Spring Boot Application

**Run As > Spring Boot App**



## 3. Add REST API and static resources

> Reference site - https://spring.io/guides/gs/spring-boot/

Create Rest API Controller

`/src/main/java/gsshop/web/jbp/controller/DashboardAPIController.java`

```java
package gsshop.web.jbp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gsshop.web.jbp.dto.Sample;

@RestController
public class DashboardAPIController {

	@RequestMapping("/api/sample/string")
	public String sampleString() {
		return "Hello World";
	}

	@RequestMapping("/api/sample/object")
	public Sample sampleObject() {
		Sample sampleObject = new Sample();
		sampleObject.setEmail("yohany@gmail.com");
		sampleObject.setName("John Kim");
		return sampleObject;
	}
}
```



Create DTO (Data Transfer Object) for responsing JSON Object within API crontroller

`/src/main/java/gsshop/web/jbp/dto/Sample.java`

```java
package gsshop.web.jbp.dto;

public class Sample {

	private String email;
	private String name;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
```



Add a sample image file for static resource services to `/src/main/resources/static/images`



And Then, Run Spring Boot Application

**Run As > Spring Boot App**



Open your Web browser with the below URLs

* http://localhost:8080/api/sample/string
* http://localhost:8080/api/sample/object
* http://localhost:8080/images/logo_gsshop.jpeg





## 4. Run MySQL with docker for test JDBC Connection

Install Docker with easy to use installers,  https://www.docker.com/products/overview

Run Mysql with https://hub.docker.com/_/mysql/

```shell
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

Create mysql sample table and insert datas

```Shell
docker exec -it mysql bash
mysql -u jbp-user -p
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
exit
```



## 5. JDBC Connection with MySQL Spring Boot Starter

> References Sites
>
> MyBatis official web site : http://www.mybatis.org/mybatis-3/
>
> MyBatis Spring Boot Starter : http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/



Add dependencies within `/pom.xml`

```xml
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



Add JDBC datasource configuration with `/src/main/resources/application.properties`

```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/jbp?autoReconnect=true&useSSL=false
spring.datasource.username=jbp-user
spring.datasource.password=jbp-user-password
```



Create SQL Mapper Interface class to retrieve datas from MySQL.

`/src/main/java/gsshop/web/jbp/mapper/SampleMapper.java`

```java
package gsshop.web.jbp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import gsshop.web.jbp.dto.Sample;

@Mapper
public interface SampleMapper {

	@Select("SELECT * FROM SAMPLE WHERE id = #{id}")
	public Sample findById(@Param("id")long id);

}

```



Modify API Controller for response user data from MySQL

`/src/main/java/gsshop/web/jbp/controller/DashboardAPIController.java`

```java
. . . . . .
import gsshop.web.jbp.mapper.SampleMapper;

public class DashboardAPIController {

	@Autowired SampleMapper sampleMapper;

    . . . . . . . . .

	@RequestMapping("/api/user/{id}")
	public Sample user(@PathVariable long id) {
		Sample sampleObject = sampleMapper.findById(id);
		return sampleObject;
	}
}
```



And Then, Run Spring Boot Application

**Run As > Spring Boot App**



Open your Web browser with the below URLs

- http://localhost:8080/api/user/1



> **TIPS!**
>
> You can change the log settings to see what you need.
>
> `/src/main/resources/application.properties`
>
> ```properties
> logging.level.org.springframework=WARN
> logging.level.gsshop.web.jbp=DEBUG
> ```



## 6. Replace Mapper Query Annotation with Query XML

> Reference Site - http://www.mybatis.org/mybatis-3/getting-started.html



Create Query Mapper XML, `/src/main/resources/mapper/SampleMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="gsshop.web.jbp.mapper.SampleMapper">
    <select id="findById" parameterType="Long" resultType="gsshop.web.jbp.dto.Sample">
        SELECT * FROM SAMPLE WHERE id = #{id}
    </select>
</mapper>
```



Remove Query annotation in Mapper Interface class

`/src/main/java/gsshop/web/jbp/mapper/SampleMapper.java`

```java
@Mapper
public interface SampleMapper {
	public Sample findById(@Param("id")long id);
}
```



Add Mapper XML Configuration with `/src/main/resources/application.properties`

```properties
mybatis.mapper-locations=mapper/**/*.xml
```



## 7. Spring Security

>  Reference Site
>
> https://spring.io/guides/gs/securing-web/
>
> https://projects.spring.io/spring-security/



Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. It is the de-facto standard for securing Spring-based applications.



Add dependencies within `/pom.xml`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```



Create Security Configuration class

`/src/main/java/gsshop/web/jbp/security/SecurityConfig.java`

```java
package gsshop.web.jbp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.formLogin();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication()
		.withUser("john").password("1234").roles("USER");
	}

}
```

> Spring Security Official Guide,  http://docs.spring.io/spring-security/site/docs/current/guides/html5//helloworld-boot.html



## 8. Create tables for Spring Security

Connect to MySQL docker instance and create User and Authority tables

```shell
docker exec -it mysql bash

mysql -u jbp-user -p

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

mysql> exit;

exit
```



## 9. Spring Security with JDBC datasource

Create User DTO impelments `org.springframework.security.core.userdetails.UserDetails`

```java
package gsshop.web.jbp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails{

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String name;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
```



Create SQL Mapper Interface

`/src/main/java/gsshop/web/jbp/security/UserMapper.java`

```java
package gsshop.web.jbp.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.GrantedAuthority;

@Mapper
public interface UserMapper {

	public User readUser(String username);
	public List<GrantedAuthority> readAuthority(String username);

	public void createUser(User user);
	public void createAuthority(User user);

}
```



Create Query Mapper XML, `/src/main/resources/mapper/UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="gsshop.web.jbp.security.UserMapper">

	<select id="readUser" parameterType="String"
		resultType="gsshop.web.jbp.security.User">
		SELECT * FROM USER WHERE username = #{username}
	</select>

	<select id="readAuthority" parameterType="String" resultType="org.springframework.security.core.authority.SimpleGrantedAuthority">
		SELECT authorityName FROM AUTHORITY WHERE username = #{username}
	</select>

	<insert id="createUser" parameterType="gsshop.web.jbp.security.User">
		INSERT INTO USER (username, password, name, isAccountNonExpired,isAccountNonLocked, isCredentialsNonExpired, isEnabled)
		VALUES (#{username}, #{password}, #{name}, #{isAccountNonExpired}, #{isAccountNonLocked}, #{isCredentialsNonExpired}, #{isEnabled})
	</insert>

	<insert id="createAuthority" parameterType="org.springframework.security.core.GrantedAuthority">
		INSERT INTO AUTHORITY (username, authorityName)
		VALUES
		<foreach item="authority" index="index" collection="authorities"
			separator=",">
			(#{username}, #{authority})
		</foreach>
	</insert>

</mapper>
```



Create TypeHandler for PreparedStatements and ResultSet.

`/src/main/java/gsshop/web/jbp/security/AuthorityTypeHandler.java`

```java
package gsshop.web.jbp.security;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class AuthorityTypeHandler extends BaseTypeHandler<SimpleGrantedAuthority> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
              SimpleGrantedAuthority parameter, JdbcType jdbcType) throws SQLException {
         ps.setString(i, parameter.getAuthority());
    }

    @Override
    public SimpleGrantedAuthority getNullableResult(ResultSet rs, String columnName)
              throws SQLException {
         return new SimpleGrantedAuthority(rs.getString(columnName));
    }

    @Override
    public SimpleGrantedAuthority getNullableResult(ResultSet rs, int columnIndex)
              throws SQLException {
         return new SimpleGrantedAuthority(rs.getString(columnIndex));
    }

    @Override
    public SimpleGrantedAuthority getNullableResult(CallableStatement cs,
              int columnIndex) throws SQLException {
         return new SimpleGrantedAuthority(cs.getString(columnIndex));
    }

}

```



Add TypeHandler configuration with `/src/main/resources/application.properties`

```properties
. . . . .
mybatis.type-handlers-package=gsshop.web.jbp.security
. . . . .
```



Create User Service Interface and Class implemented.

`/src/main/java/gsshop/web/jbp/security/UserService.java`

```java
package gsshop.web.jbp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService extends UserDetailsService{

    Collection<GrantedAuthority> getAuthorities(String username);

    public void createUser(User user);

    public PasswordEncoder passwordEncoder();

}
```

`/src/main/java/gsshop/web/jbp/security/UserServiceImpl.java`

```java
package gsshop.web.jbp.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.readUser(username);
		if(user != null) user.setAuthorities(getAuthorities(username));
		return user;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities(String username) {
		Collection<GrantedAuthority> authorities = userMapper.readAuthority(username);
		return authorities;
	}

	@Override
	public void createUser(User user) {
		String rawPassword = user.getPassword();
		String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
		user.setPassword(encodedPassword);
		userMapper.createUser(user);
		userMapper.createAuthority(user);
	}

	@Override
	public PasswordEncoder passwordEncoder() {
		return this.passwordEncoder;
	}

}
```

> BCryptPasswordEncoder is the implementation of PasswordEncoder that uses the BCrypt strong hashing function



Modify Security Configuration class, `/src/main/java/gsshop/web/jbp/security/SecurityConfig.java`

```java
. . . . . . .

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired UserService userService;

	. . . . . . .

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userService)
		.passwordEncoder(userService.passwordEncoder());
	}

}
```



## 10. Test UserSerive

`/src/test/java/gsshop/web/jbp/security/UserServiceTest.java`

```java
package gsshop.web.jbp.security;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gsshop.web.jbp.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

	@Autowired private UserService userService;

	private User user;

	@Before
	public void setup() {
		user = new User();
		user.setUsername("johnkim");
		user.setPassword("password");
		user.setName("John Kim");
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
	}

	@Test
	public void createUserTest() {

		User checkUser = (User) userService.loadUserByUsername(user.getUsername());

		if(checkUser == null) {

			userService.createUser(user);

			User existedUser = (User) userService.loadUserByUsername(user.getUsername());
			assertThat(user.getUsername(), is(existedUser.getUsername()));

			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
			Iterator<? extends GrantedAuthority> it = authorities.iterator();
			while (it.hasNext()) {
				GrantedAuthority authority = it.next();
				assertThat(authorities, hasItem(new SimpleGrantedAuthority(authority.getAuthority())));
			}
		}
	}			
}
```

**Run As > JUnit Test**



## 11. UI Template (Handlebars)



Copy **/resources/static** folder to `/src/main/resources/static`



Copy **/resources/templates** folder to `/src/main/resources/templates`



Import `handlebars.spring.boot.starter` project with Ecilpse.



Add dependencies within `/pom.xml`

```xml
<dependency>
  <groupId>gsshop.boot</groupId>
  <artifactId>handlebars-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```



Modify Security Configuration

`/src/main/java/gsshop/web/jbp/security/SecurityConfig.java`

```java
package gsshop.web.jbp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired UserService userService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/lib/**", "/images/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
        .authorizeRequests()
            .antMatchers("/").permitAll() // public pages
            .anyRequest().authenticated() // authorized pages
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout()
            .permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userService)
		.passwordEncoder(userService.passwordEncoder());
	}

}
```



Modify Application Class

`/src/main/java/gsshop/web/jbp/Application.java`

```java
package gsshop.web.jbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("redirect:/dashboard");
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```



> **Tips**
>
> Set Non-Cache Template configuration in `application.properties`
>
> ```properties
> handlebars.cache=false
> ```





## 12. Spring Acurator



> http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-monitoring.html

Add dependencies within `/pom.xml`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



`/src/main/java/gsshop/web/jbp/security/SecurityConfig.java`

```java
.antMatchers("/manage/**").hasRole("ACTUATOR")
```



Connect to MySQL docker instance and insert data for Authority

```shell
docker exec -it mysql bash

mysql -u jbp-user -p

mysql> use jbp

mysql> INSERT INTO AUTHORITY VALUES ('johnkim', 'ACTUATOR');

mysql> exit;

exit
```



# More Tips

**Default Application configuration**

>  https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html



**Spring-Loaded**

`pom.xml`

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>springloaded</artifactId>
            <version>1.2.6.RELEASE</version>
        </dependency>
    </dependencies>
</plugin>
```

> http://docs.spring.io/spring-boot/docs/current/reference/html/howto-hotswapping.html#howto-reload-springloaded-maven



```
mvn spring-boot:run
```





**Logging**

```java
private static final Logger logger = LoggerFactory.getLogger(CLASSNAME.class);

logger.debug("DEBUG");
```
