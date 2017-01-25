package gsshop.jbp.dashboard.security;

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

import gsshop.jbp.dashboard.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.RANDOM_PORT) 
public class UserServiceTest {

	@Autowired private UserService userService;

	private User user;

	@Before
	public void setup() {
		user = new User();
		user.setUsername("user1");
        user.setPassword("pass1");
        user.setName("USER1");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
	}
	
	@Test
	public void createUserTest() {
		userService.deleteUser(user.getUsername());
		userService.createUser(user);
		
		User existedUser = userService.readUser(user.getUsername());
		assertThat(user.getUsername(), is(existedUser.getUsername()));
		
		PasswordEncoder passwordEncoder = userService.passwordEncoder();
		assertThat(passwordEncoder.matches("pass1", user.getPassword()), is(true));
		
		Collection<? extends GrantedAuthority> authorities1 = user.getAuthorities();
        Iterator<? extends GrantedAuthority> it = authorities1.iterator();
        
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        while (it.hasNext()) {
             GrantedAuthority authority = it.next();
             assertThat(authorities, hasItem(new SimpleGrantedAuthority(authority.getAuthority())));
        }

		
	}			
}
