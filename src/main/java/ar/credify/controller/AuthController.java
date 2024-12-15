package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class AuthController {
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


    //   ...................... Register ............................

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        RegistrationForm form = new RegistrationForm();
        Person person = new Person();
        person.setGender("");
        form.setPerson(person);
        form.setUser(new UserAccount());
        form.setRole("");
        form.setOrganization(new Organization());
        model.addAttribute("registrationForm", form);
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                           BindingResult bindingResult) {

        if (personService.getPersonByEmail(form.getPerson().getEmailAddress()) != null) {
            bindingResult.rejectValue("person.emailAddress", "error.emailAddress", "Email address already exists.");
        }

        if (userAccountService.getUserByUsername(form.getUser().getUsername()) != null) {
            bindingResult.rejectValue("user.username", "error.username", "Username is already taken.");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        personService.registerPerson(form.getPerson());

        Role role = roleService.getRoleByName(form.getRole());

        UserAccount account = new UserAccount();
        account.setUsername(form.getUser().getUsername());
        account.setPassword(passwordEncoder.encode(form.getUser().getPassword()));
        account.setPerson(form.getPerson());
        account.setRole(role);
        userAccountService.registerUser(account);

        if(role.getName().equals("University-Admin")) {
            Organization org = new Organization();
            org.setName(form.getOrganization().getName());
            org.setOrgType(OrganizationType.University);
            org.setCountry(form.getOrganization().getCountry());
            organizationService.save(org);

            OrganizationEmployee organizationEmployee = new OrganizationEmployee();
            organizationEmployee.setUser(account);
            organizationEmployee.setOrganization(org);
            organizationEmployeeService.save(organizationEmployee);
        } else if (role.getName().equals("Employer-Admin")) {
            Organization org = new Organization();
            org.setName(form.getOrganization().getName());
            org.setOrgType(OrganizationType.Employer);
            org.setCountry(form.getOrganization().getCountry());
            organizationService.save(org);

            OrganizationEmployee organizationEmployee = new OrganizationEmployee();
            organizationEmployee.setUser(account);
            organizationEmployee.setOrganization(org);
            organizationEmployeeService.save(organizationEmployee);
        }
        return "redirect:/registrationSuccess";
    }

    @GetMapping("/registrationSuccess")
    public String registerSuccess() {
        return "auth/registration-success";
    }


    //   ...................... Login ............................

    @GetMapping("/login")
    public String showLoginForm(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getAuthorities().isEmpty()) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Student"))) {
                response.sendRedirect("/student/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_University-Admin"))) {
                response.sendRedirect("/university/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Professor"))) {
                response.sendRedirect("/professor/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Registrar"))) {
                response.sendRedirect("/registrar/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Admission-Committee"))) {
                response.sendRedirect("/admissionCommittee/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Employer-Admin"))) {
                response.sendRedirect("/employer/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Recruiter"))) {
                response.sendRedirect("/recruiter/dashboard");
                return null;
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Hiring-Manager"))) {
                response.sendRedirect("/hiringManager/dashboard");
                return null;
            }
        }
        return "auth/login";
    }
}
