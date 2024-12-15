package ar.credify.serviceimpl;

import ar.credify.model.UserAccount;
import ar.credify.model.Role;  // Ensure you have the Role class
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAccountDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountService.getUserByUsername(username);

        if (userAccount == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Role role = userAccount.getRole();
        String roleName = "ROLE_" + role.getName();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return org.springframework.security.core.userdetails.User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }
}