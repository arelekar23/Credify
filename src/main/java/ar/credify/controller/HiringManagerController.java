package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.JobRoleService;
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
@RequestMapping("/hiringManager")
public class HiringManagerController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private JobRoleService jobRoleService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // .................................. Profile .....................................

    @GetMapping("/profile")
    public String recruiterProfile(Model model) {
        Person person = userAccountService.getCurrentUser().getPerson();
        model.addAttribute("person", person);
        return "hiringManager/profile";
    }

    @PostMapping("/profile/update")
    public String updateUniversityProfile(@ModelAttribute("person") Person person) {
        person.setId(userAccountService.getCurrentUser().getPerson().getId());
        personService.updatePerson(person);
        return "redirect:/hiringManager/profile";
    }

    @GetMapping("/profile/update-password")
    public String showUpdatePasswordForm(Model model) {
        return "hiringManager/update-password";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/hiringManager/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/hiringManager/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/hiringManager/dashboard";
    }

    // .................................. Manage Job Roles .....................................

    @GetMapping("/jobs")
    public String jobApplications(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        model.addAttribute("newJob", new JobRole());
        model.addAttribute("jobList", jobRoleService.getJobListForOrg(org));
        return "hiringManager/jobs";
    }

    @PostMapping("/jobs/add")
    public String addJobRole(@ModelAttribute JobRole jobRole) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        jobRole.setEmployer(org);
        jobRoleService.saveJob(jobRole);
        return "redirect:/hiringManager/jobs";
    }

    @GetMapping("/jobs/edit/{id}")
    public String editJobRole(@PathVariable Long id, Model model) {
        JobRole jobRole = jobRoleService.findJobById(id);
        model.addAttribute("jobRole", jobRole);
        return "hiringManager/edit-job";
    }

    @PostMapping("/jobs/update")
    public String updateJobRole(@ModelAttribute JobRole jobRole) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        jobRole.setEmployer(org);
        jobRoleService.updateJob(jobRole);
        return "redirect:/hiringManager/jobs";
    }

    @PostMapping("/jobs/delete/{id}")
    public String deleteJobRole(@PathVariable Long id) {
        JobRole jobRole = jobRoleService.findJobById(id);
        jobRoleService.deleteJob(jobRole);
        return "redirect:/hiringManager/jobs";
    }
}
