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
