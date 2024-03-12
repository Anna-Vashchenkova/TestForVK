package test.user;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User saveUser(Long id, String email, String name);

    User updateUser(Long userId, User user);

    User getUserById(Long userId);

    void deleteUserById(Long userId);
}
