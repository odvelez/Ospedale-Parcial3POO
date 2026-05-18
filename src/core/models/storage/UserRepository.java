package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.User;
import java.util.ArrayList;

public interface UserRepository {

    ArrayList<User> getUsers();

    User findById(long id);

    User findByUsername(String username);

    boolean add(User user);

    User getCurrentUser();

    void setCurrentUser(User user);
}
