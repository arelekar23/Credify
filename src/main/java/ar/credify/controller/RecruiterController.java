package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private JobApplicationsService jobApplicationsService;
    @Autowired
    private EmploymentService employmentService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String recruiterProfile(Model model) {
        Person person = userAccountService.getCurrentUser().getPerson();
        model.addAttribute("person", person);
        return "recruiter/profile";
    }

    @PostMapping("/profile/update")
    public String updateUniversityProfile(@ModelAttribute("person") Person person) {
        person.setId(userAccountService.getCurrentUser().getPerson().getId());
        personService.updatePerson(person);
        return "redirect:/recruiter/profile";
    }

    @GetMapping("/profile/update-password")
    public String showUpdatePasswordForm(Model model) {
        return "recruiter/update-password";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/recruiter/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/recruiter/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/recruiter/dashboard";
    }

    @GetMapping("/applications")
    public String manageIncomingApplications(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        List<JobApplications> applicationList = jobApplicationsService.fetchApplicationsByUniversity(org);
        model.addAttribute("applicationList",applicationList);
        return "recruiter/incomingApplications";
    }

    @PostMapping("/application/accept/{id}")
    public String acceptApplication(@PathVariable Long id) {
        JobApplications application = jobApplicationsService.findJobApplicationById(id);
        if (application != null) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            jobApplicationsService.saveJobApplication(application);
        }
        return "redirect:/recruiter/applications";
    }

    @PostMapping("/application/reject/{id}")
    public String rejectApplication(@PathVariable Long id) {
        JobApplications application = jobApplicationsService.findJobApplicationById(id);
        if (application != null) {
            application.setStatus(ApplicationStatus.REJECTED);
            jobApplicationsService.saveJobApplication(application);
        }
        return "redirect:/recruiter/applications";
    }

    @GetMapping("/application/details/{id}")
    public String viewApplicationDetails(@PathVariable Long id, Model model) {
        JobApplications application = jobApplicationsService.findJobApplicationById(id);

        if (application != null) {
            jobApplicationsService.saveJobApplication(application);
        }
        return "redirect:/recruiter/applications";
    }

    @GetMapping("/verificationRequests")
    public String manageVerificationRequests(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        List<Employment> employmentList = employmentService.fetchEmploymentListForEmployer(org.getId());
        model.addAttribute("employmentList",employmentList);
        return "recruiter/verificationRequests";
    }

    @PostMapping("/jobs/verify/{id}")
    public String verifyJob(@PathVariable Long id) {
        Employment employment = employmentService.findEmploymentById(id);
        employment.setIsVerified(true);
        employmentService.saveEmployment(employment);

        JobApplications existingApplication = jobApplicationsService.fetchByJobRole(employment.getPosition(), employment.getUser());

        if (existingApplication != null) {
            existingApplication.setStatus(ApplicationStatus.COMPLETED);
            jobApplicationsService.saveJobApplication(existingApplication);
        } else {
            JobApplications newApplication = new JobApplications();
            newApplication.setPosition(employment.getPosition());
            newApplication.setStudent(employment.getUser());
            newApplication.setStatus(ApplicationStatus.COMPLETED);
            jobApplicationsService.saveJobApplication(newApplication);
        }
        return "redirect:/recruiter/verificationRequests";
    }
}
