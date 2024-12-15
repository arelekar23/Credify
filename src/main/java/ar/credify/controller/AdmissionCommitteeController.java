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
@RequestMapping("/admissionCommittee")
public class AdmissionCommitteeController {
    @Autowired
    private EducationService educationService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UniversityApplicationService universityApplicationService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;


//   ...................... Profile ............................

    @GetMapping("/profile")
    public String universityProfile(Model model) {
        Person person = userAccountService.getCurrentUser().getPerson();
        model.addAttribute("person", person);
        return "admissionCommittee/profile";
    }

    @PostMapping("/profile/update")
    public String updateUniversityProfile(@ModelAttribute("person") Person person) {
        person.setId(userAccountService.getCurrentUser().getPerson().getId());
        personService.updatePerson(person);
        return "redirect:/admissionCommittee/profile";
    }

    @GetMapping("/profile/update-password")
    public String showUpdatePasswordForm() {
        return "admissionCommittee/update-password";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/admissionCommittee/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/admissionCommittee/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/admissionCommittee/dashboard";
    }

//   ...................... Verification Requests ............................

    @GetMapping("/verificationRequests")
    public String manageVerificationRequests(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        List<Education> educationList = educationService.fetchEducationListForUniversity(org.getId());
        model.addAttribute("educationList",educationList);
        return "admissionCommittee/verificationRequests";
    }

    @PostMapping("/education/verify/{id}")
    public String verifyEducation(@PathVariable Long id) {
        Education education = educationService.findById(id);
        education.setVerified(true);
        educationService.save(education);

        UniversityApplication existingApplication = universityApplicationService.fetchByMajor(education.getMajor(), education.getStudent());

        if (existingApplication != null) {
            existingApplication.setStatus(ApplicationStatus.COMPLETED);
            universityApplicationService.save(existingApplication);
        } else {
            UniversityApplication newApplication = new UniversityApplication();
            newApplication.setMajor(education.getMajor());
            newApplication.setStudent(education.getStudent());
            newApplication.setStatus(ApplicationStatus.COMPLETED);
            universityApplicationService.save(newApplication);
        }

        return "redirect:/admissionCommittee/verificationRequests";
    }

    //   ...................... Incoming Applications ............................

    @GetMapping("/applications")
    public String manageIncomingApplications(Model model) {
        Organization org = organizationService.findOrganizationByUser(userAccountService.getCurrentUser());
        List<UniversityApplication> applicationList = universityApplicationService.fetchAllByUniversity(org);
        model.addAttribute("applicationList",applicationList);
        return "admissionCommittee/incomingApplications";
    }

    @PostMapping("/application/accept/{id}")
    public String acceptApplication(@PathVariable Long id) {
        UniversityApplication application = universityApplicationService.findById(id);
        if (application != null) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            universityApplicationService.save(application);
        }
        return "redirect:/admissionCommittee/applications";
    }

    @PostMapping("/application/reject/{id}")
    public String rejectApplication(@PathVariable Long id) {
        UniversityApplication application = universityApplicationService.findById(id);
        if (application != null) {
            application.setStatus(ApplicationStatus.REJECTED);
            universityApplicationService.save(application);
        }
        return "redirect:/admissionCommittee/applications";
    }

    @GetMapping("/application/details/{id}")
    public String viewApplicationDetails(@PathVariable Long id) {
        UniversityApplication application = universityApplicationService.findById(id);

        if (application != null) {
            universityApplicationService.save(application);
        }
        return "redirect:/admissionCommittee/applications";
    }
}
