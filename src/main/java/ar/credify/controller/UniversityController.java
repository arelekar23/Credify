package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class UniversityController {
    @Autowired
    private PersonService personService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationEmployeeService organizationEmployeeService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/university/profile")
    public String universityProfile(Model model) {
        Person person = getCurrentPerson();
        Organization university = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        model.addAttribute("person", person);
        model.addAttribute("university", university);
        return "university/profile";
    }

    @PostMapping("/university/profile/update")
    public String updateUniversityProfile(@ModelAttribute("person") Person person) {
        person.setId(getCurrentPerson().getId());
        personService.updatePerson(person);
        return "redirect:/university/profile";
    }

    @GetMapping("/university/manage-users")
    public String universityManageProfiles(Model model) {
        UserAccount currentUser = userAccountService.getCurrentUser();
        Organization currentOrg = organizationService.findOrganizationByUser(currentUser);
        List<OrganizationEmployee> usersList = organizationEmployeeService.fetchUsers(currentOrg);
        model.addAttribute("form", new RegistrationForm());
        model.addAttribute("usersList", usersList);
        return "university/manage-profiles-new";
    }

    @PostMapping("/university/users/add")
    public String addUser(@Valid @ModelAttribute RegistrationForm form){
        personService.registerPerson(form.getPerson());

        Role role = roleService.getRoleByName(form.getRole());

        UserAccount account = new UserAccount();
        account.setUsername(form.getUser().getUsername());
        account.setPassword(passwordEncoder.encode(form.getUser().getPassword()));
        account.setPerson(form.getPerson());
        account.setRole(role);
        userAccountService.registerUser(account);

        UserAccount currentUser = userAccountService.getCurrentUser();
        Organization currentOrg = organizationService.findOrganizationByUser(currentUser);

        OrganizationEmployee organizationEmployee = new OrganizationEmployee();
        organizationEmployee.setUser(account);
        organizationEmployee.setOrganization(currentOrg);

        organizationEmployeeService.save(organizationEmployee);
        return "redirect:/university/manage-users";
    }

    @GetMapping("/university/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        OrganizationEmployee organizationEmployee = organizationEmployeeService.findById(id);
        UserAccount userAccount = organizationEmployee.getUser();
        Person person = userAccount.getPerson();
        Organization organization = organizationService.findOrganizationByUser(userAccount);

        RegistrationForm form = new RegistrationForm();
        form.setPerson(person);
        form.setUser(userAccount);;
        form.setRole(userAccount.getRole().getName());
        form.setOrganization(organization);

        model.addAttribute("form", form);
        return "university/edit-profile";
    }

    @PostMapping("/university/users/update")
    public String updateUser(@ModelAttribute RegistrationForm form) {
        Person person = form.getPerson();
        personService.updatePerson(person);
        return "redirect:/university/manage-users";
    }

    @PostMapping("/university/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        OrganizationEmployee orgEmployee = organizationEmployeeService.findById(id);
        UserAccount userAccount = orgEmployee.getUser();
        Person person = userAccount.getPerson();

        organizationEmployeeService.deleteUser(orgEmployee);
        userAccountService.deleteUser(userAccount);
        personService.deletePerson(person);

        return "redirect:/university/manage-users";
    }
    public Person getCurrentPerson() {
        Person person = personService.findById(userAccountService.getCurrentUser().getPerson().getId());
        return person;
    }

}
