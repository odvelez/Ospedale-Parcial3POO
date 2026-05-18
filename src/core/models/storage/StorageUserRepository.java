package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.User;
import java.util.ArrayList;

public class StorageUserRepository implements UserRepository {

    private final Storage storage;

    public StorageUserRepository() {
        this.storage = Storage.getInstance();
    }

    @Override
    public ArrayList<User> getUsers() {
        return storage.getUsers();
    }

    @Override
    public User findById(long id) {
        return storage.findUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        return storage.findUserByUsername(username);
    }

    @Override
    public boolean add(User user) {
        return storage.addUser(user);
    }

    @Override
    public User getCurrentUser() {
        return storage.getCurrentUser();
    }

    @Override
    public void setCurrentUser(User user) {
        storage.setCurrentUser(user);
    }
}
