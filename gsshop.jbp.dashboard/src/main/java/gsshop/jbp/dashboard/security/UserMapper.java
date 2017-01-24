package gsshop.jbp.dashboard.security;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.GrantedAuthority;

@Mapper
public interface UserMapper {

	public User readUser(String username);
    public List<GrantedAuthority> readAuthority(String username);

    public void createUser(User user);
    public void createAuthority(User user);
    public void deleteUser(String username);
    public void deleteAuthority(String username);

}
