package series.users.services;
import series.users.models.User;

import java.util.List;
public interface IUserService {
    public List<User> findById(Long id);
    public List<User> findByUserName(String username);
    public List<User> getAll();
    public void createUser(String username, String password, String Role);
    public User login(String username, String password);
}
