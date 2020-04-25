package series.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import series.users.models.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements  IUserService{
    @Autowired
    JdbcTemplate jdbctemp;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    @Cacheable("users")
    public List<User> getAll() {
        String req = "SELECT * FROM USERS";
        List<User> users = jdbctemp.query(req, new BeanPropertyRowMapper(User.class));
        return users;
    }
    @Override
    public List<User> findById(Long id) {
        String req = "SELECT * FROM USERS WHERE ID=?";
        return jdbctemp.query(req, new Object[]{id}, new BeanPropertyRowMapper(User.class));
    }

    @Override
    public List<User> findByUserName(String username) {
        String req = "SELECT * FROM USERS WHERE USERNAME='"+username+"'";
        return jdbctemp.query(req, new BeanPropertyRowMapper(User.class));
    }

    @Override
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public void createUser(String username, String password, String role) {
        String req = "INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('"+username+"','"+
                encoder.encode(password)+"','"+role+"')";
        jdbctemp.execute(req);
    }

    @Override
    public User login(String username, String password) {
        String req = "SELECT * FROM USERS WHERE USERNAME='"+ username+"'";
        List<User> users = jdbctemp.query(req, new BeanPropertyRowMapper(User.class));
        Optional<User> user = users.stream().filter(u -> encoder.matches(password,u.getPassword())).findFirst();
        return user.get();
    }
}
