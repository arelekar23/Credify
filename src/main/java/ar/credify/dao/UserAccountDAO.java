package ar.credify.dao;

import ar.credify.model.UserAccount;
import org.apache.catalina.User;

import java.util.List;

public interface UserAccountDAO {
    void save(UserAccount userAccount);
    void delete(UserAccount userAccount);
    UserAccount findByUsername(String username);
    UserAccount findUserById(Long id);
    List<UserAccount> fetchAllUsers();

}