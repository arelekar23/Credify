package ar.credify.serviceimpl;

import ar.credify.dao.UserAccountDAO;
import ar.credify.model.UserAccount;
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountDAO userAccountDAO;

    @Override
    @Transactional
    public void registerUser(UserAccount userAccount) {
        userAccountDAO.save(userAccount);
    }

    @Override
    @Transactional
    public void deleteUser(UserAccount userAccount) {
        userAccountDAO.delete(userAccount);
    }

    @Override
    @Transactional
    public void updateUser(UserAccount userAccount) {
        userAccountDAO.save(userAccount);
    }


    @Override
    @Transactional
    public UserAccount getUserByUsername(String username) {
        return userAccountDAO.findByUsername(username);
    }

    @Transactional
    public void updatePassword(UserAccount userAccount) {
        userAccountDAO.save(userAccount);
    }

    @Override
    public UserAccount findUserById(Long id) {
        return userAccountDAO.findUserById(id);
    }

    @Override
    public UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = getUserByUsername(authentication.getName());
        return userAccount;
    }

    @Override
    public List<UserAccount> fetchAllUsers() {
        return userAccountDAO.fetchAllUsers();
    }
}