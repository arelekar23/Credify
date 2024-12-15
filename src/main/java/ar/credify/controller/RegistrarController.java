package ar.credify.controller;

import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.model.Person;
import ar.credify.model.UserAccount;
import ar.credify.service.MajorService;
import ar.credify.service.OrganizationService;
import ar.credify.service.PersonService;
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrarController {

    @Autowired
    private MajorService majorService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registrar/profile")
    public String universityProfile(Model model) {
        Person person = userAccountService.getCurrentUser().getPerson();
        model.addAttribute("person", person);
        return "registrar/profile";
    }

    @PostMapping("/registrar/profile/update")
    public String updateUniversityProfile(@ModelAttribute("person") Person person) {
        person.setId(userAccountService.getCurrentUser().getPerson().getId());
        personService.updatePerson(person);
        return "redirect:/registrar/profile";
    }


    @GetMapping("/registrar/profile/update-password")
    public String showUpdatePasswordForm() {
        return "registrar/update-password";
    }

    @PostMapping("/registrar/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/registrar/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/registrar/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/registrar/dashboard";
    }

    @GetMapping("/registrar/courses")
    public String registrarCourses(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        model.addAttribute("newMajor", new Major());
        model.addAttribute("majors", majorService.getCourseListForOrg(org));
        return "registrar/courses";
    }

    @PostMapping("/registrar/courses/add")
    public String addCourse(@ModelAttribute Major major) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        major.setOrganization(org);
        majorService.save(major);
        return "redirect:/registrar/courses";
    }

    @GetMapping("/registrar/courses/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        Major major = majorService.findById(id);
        model.addAttribute("major", major);
        return "registrar/edit-course";
    }

    @PostMapping("/registrar/courses/update")
    public String updateCourse(@ModelAttribute Major major) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        major.setOrganization(org);
        majorService.save(major);
        return "redirect:/registrar/courses";
    }

    @PostMapping("/registrar/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        Major major = majorService.findById(id);
        majorService.deleteCourse(major);
        return "redirect:/registrar/courses";
    }
}