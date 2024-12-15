package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class OrganizationUserController {
    @Autowired
    private PersonService personService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationEmployeeService organizationEmployeeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/list")
    public List<OrganizationEmployee> getUserList() {
        UserAccount currentUser = getCurrentUser();
        Organization currentOrg = organizationService.findOrganizationByUser(currentUser);
        return organizationEmployeeService.fetchUsers(currentOrg);
    }
    @GetMapping("/by-role")
    public List<OrganizationEmployee> getUsersByRole(@RequestParam String role) {
        UserAccount currentUser = getCurrentUser();
        Organization currentOrg = organizationService.findOrganizationByUser(currentUser);
        Role userRole = roleService.getRoleByName(role);
        return organizationEmployeeService.fetchUsersByRole(currentOrg, userRole);
    }

    @GetMapping("/details")
    public UserAccount getUserDetails(@RequestParam Long id) {
        return userAccountService.findUserById(id);
    }

    @PostMapping("/create")
    public String createNewUser(@Valid @RequestBody RegistrationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Error: " + bindingResult.getFieldErrors();
        }

        personService.registerPerson(form.getPerson());

        Role role = roleService.getRoleByName(form.getRole());

        UserAccount account = new UserAccount();
        account.setUsername(form.getUser().getUsername());
        account.setPassword(passwordEncoder.encode(form.getUser().getPassword()));
        account.setPerson(form.getPerson());
        account.setRole(role);
        userAccountService.registerUser(account);

        UserAccount currentUser = getCurrentUser();
        Organization currentOrg = organizationService.findOrganizationByUser(currentUser);

        OrganizationEmployee organizationEmployee = new OrganizationEmployee();
        organizationEmployee.setUser(account);
        organizationEmployee.setOrganization(currentOrg);

        organizationEmployeeService.save(organizationEmployee);

        return "User created successfully";
    }

    @PutMapping("/edit")
    public String editUser(@Valid @RequestBody RegistrationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Error: " + bindingResult.getFieldErrors();
        }
        Person person = personService.getPersonByEmail(form.getPerson().getEmailAddress());
        Person newPerson = form.getPerson();
        newPerson.setId(person.getId());
        personService.updatePerson(newPerson);

        return "User updated successfully";
    }
    public UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        return userAccountService.getUserByUsername(user);
    }
}
