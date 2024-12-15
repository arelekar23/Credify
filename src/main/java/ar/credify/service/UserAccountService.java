package ar.credify.service;

import ar.credify.model.UserAccount;

import java.util.List;

public interface UserAccountService {
    void registerUser(UserAccount userAccount);
    void deleteUser(UserAccount userAccount);
    void updateUser(UserAccount userAccount);
    UserAccount getUserByUsername(String username);
    void updatePassword(UserAccount userAccount);
    UserAccount findUserById(Long id);
    UserAccount getCurrentUser();
    List<UserAccount> fetchAllUsers();

}