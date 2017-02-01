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
