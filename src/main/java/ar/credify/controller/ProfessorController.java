package ar.credify.controller;

import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Person;
import ar.credify.model.Project;
import ar.credify.model.UserAccount;
import ar.credify.service.OrganizationEmployeeService;
import ar.credify.service.PersonService;
import ar.credify.service.ProjectService;
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProfessorController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private OrganizationEmployeeService organizationEmployeeService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //................................ Profile .....................................

    @GetMapping("/professor/profile")
    public String professorProfile(Model model) {
        UserAccount userAccount = userAccountService.getCurrentUser();
        Person person = userAccount.getPerson();
        model.addAttribute("person", person);
        return "professor/profile";
    }

    @PostMapping("/professor/profile/update")
    public String updateProfile(@ModelAttribute("person") Person person) {
        personService.updatePerson(person);
        return "redirect:/professor/profile";
    }

    @GetMapping("/professor/profile/update-password")
    public String showUpdatePasswordForm(Model model) {
        return "professor/update-password";
    }

    @PostMapping("/professor/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/professor/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/professor/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/professor/dashboard";
    }

    //................................ Project Verification Requests .....................................

    @GetMapping("/professor/projects")
    public String professorProjects(Model model) {
        UserAccount userAccount = userAccountService.getCurrentUser();
        OrganizationEmployee organizationEmployee = organizationEmployeeService.findByUserId(userAccount);
        List<Project> projects = projectService.fetchAllAcademicProjectsByProfessor(organizationEmployee);
        model.addAttribute("projects",projects);
        return "professor/projects";
    }

    @PostMapping("/professor/projects/verify")
    public String verifyProject(@RequestParam Long projectId) {
        Project project = projectService.findById(projectId);
        project.setIsVerified(true);
        projectService.save(project);
        return "redirect:/professor/projects";
    }
}
