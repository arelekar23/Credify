package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.*;
import ar.credify.serviceimpl.StudentDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private PersonService personService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EducationService educationService;
    @Autowired
    private JobRoleService jobRoleService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UniversityApplicationService universityApplicationService;
    @Autowired
    private JobApplicationsService jobApplicationsService;
    @Autowired
    private EmploymentService employmentService;
    @Autowired
    private StudentDocumentService studentDocumentService;

    //.................................. Profile ..........................................

    @GetMapping("/student/profile")
    public String studentProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);
        Person person = userAccount.getPerson();
        System.out.println("ID: " + person.getId());
        model.addAttribute("person", person);
        return "student/profile";
    }
    @PostMapping("/student/profile/update")
    public String updateProfile(@ModelAttribute("person") Person person) {
        personService.updatePerson(person);
        return "redirect:/student/profile";
    }

    @GetMapping("/student/profile/update-password")
    public String showUpdatePasswordForm(Model model) {
        return "student/update-password";
    }

    @PostMapping("/student/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/student/profile/update-password?error=Passwords%20do%20not%20match";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        UserAccount userAccount = userAccountService.getUserByUsername(user);

        if (!passwordEncoder.matches(currentPassword, userAccount.getPassword())) {
            return "redirect:/student/profile/update-password?error=Invalid%20current%20password";
        }
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountService.updatePassword(userAccount);

        return "redirect:/student/dashboard";
    }

    //.................................. Education ..........................................
    @GetMapping("/student/education")
    public String studentEducation(Model model) {
        model.addAttribute("newEducation", new Education());
        model.addAttribute("educationList", educationService.getEducationListForStudent(userAccountService.getCurrentUser()));
        return "student/education";
    }

    @PostMapping("/student/education/add")
    public String addEducation(@ModelAttribute("newEducation") Education education, Model model) {
        UserAccount student = userAccountService.getCurrentUser();
        education.setStudent(student);
        education.setVerified(false);

        List<Education> existingEducations = educationService.findByStudentId(student.getId());
        Organization university = majorService.findOrgByMajorId(education.getMajor().getId());
        Major major = majorService.findById(education.getMajor().getId());
        for (Education existingEducation : existingEducations) {
            String existingEducationUniversity = existingEducation.getMajor().getOrganization().getName();
            String newEducationUniversity = university.getName();

            String existingMajor = existingEducation.getMajor().getName();
            String newMajor = major.getName();
            if (existingEducationUniversity.equals(newEducationUniversity) &&
                    existingMajor.equals(newMajor)) {
                model.addAttribute("error", "Education already exists for this university and major.");
                return "student/education";
            }
        }
        educationService.save(education);
        return "redirect:/student/education";
    }

    @GetMapping("/student/education/edit/{id}")
    public String editEducation(@PathVariable Long id, Model model) {
        Education education = educationService.findById(id);
        model.addAttribute("education", education);
        return "student/edit-education";
    }

    @PostMapping("/student/education/update")
    public String updateEducation(@ModelAttribute Education education) {
        education.setStudent(userAccountService.getCurrentUser());
        education.setVerified(false);
        educationService.save(education);
        return "redirect:/student/education";
    }

    @PostMapping("/student/education/delete/{id}")
    public String deleteEducation(@PathVariable Long id) {
        Education education = educationService.findById(id);
        educationService.deleteEducation(education);
        return "redirect:/student/education";
    }

    //......................................Projects.....................................

    @GetMapping("/student/project")
    public String studentProject(Model model) {
        model.addAttribute("newProject", new Project());
        model.addAttribute("projectsList", projectService.fetchProjectsByUser(userAccountService.getCurrentUser()));
        return "student/project";
    }

    @PostMapping("/student/project/add")
    public String addNewProject(@ModelAttribute Project project) {
        project.setStudent(userAccountService.getCurrentUser());
        project.setIsVerified(false);
        projectService.save(project);
        return "redirect:/student/project";
    }
    @GetMapping("/student/project/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id);
        model.addAttribute("project", project);
        return "student/edit-project";
    }

    @PostMapping("/student/project/update")
    public String updateProject(@ModelAttribute Project project) {
        project.setStudent(userAccountService.getCurrentUser());
        project.setIsVerified(false);
        projectService.save(project);
        return "redirect:/student/project";
    }

    @PostMapping("/student/project/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        Project project = projectService.findById(id);
        projectService.deleteProject(project);
        return "redirect:/student/project";
    }

    //...................................Employment......................................

    @GetMapping("/student/employment")
    public String studentEmployment(Model model) {
        model.addAttribute("newEmployment", new Employment());
        model.addAttribute("employmentList", employmentService.getEmploymentListForStudent(userAccountService.getCurrentUser()));
        return "student/employment";
    }

    @PostMapping("/student/employment/add")
    public String addEmployment(@ModelAttribute Employment employment, Model model) {
        UserAccount student = userAccountService.getCurrentUser();
        employment.setUser(student);
        employment.setIsVerified(false);

        List<Employment> existingEmployments = employmentService.findByStudentId(student.getId());

        Organization organization = jobRoleService.findOrgByJobRoleId(employment.getPosition().getId());
        JobRole jobRole = jobRoleService.findJobById(employment.getPosition().getId());

        for (Employment existingEmployment : existingEmployments) {
            String existingEmploymentOrganization = existingEmployment.getPosition().getEmployer().getName();
            String newEmploymentOrganization = organization.getName();

            String existingPosition = existingEmployment.getPosition().getName();
            String newPosition = jobRole.getName();

            if (existingEmploymentOrganization.equals(newEmploymentOrganization) &&
                    existingPosition.equals(newPosition)) {
                model.addAttribute("newEmployment", new Employment());
                model.addAttribute("employmentList", employmentService.getEmploymentListForStudent(userAccountService.getCurrentUser()));
                model.addAttribute("error", "Employment already exists for this organization and position.");
                return "student/employment";
            }
        }

        employmentService.saveEmployment(employment);

        return "redirect:/student/employment";
    }

    @GetMapping("/student/employment/edit/{id}")
    public String editEmployment(@PathVariable Long id, Model model) {
        Employment employment = employmentService.findEmploymentById(id);
        model.addAttribute("employment", employment);
        return "student/edit-employment";
    }

    @PostMapping("/student/employment/update")
    public String updateEmployment(@ModelAttribute Employment employment) {
        employment.setUser(userAccountService.getCurrentUser());
        employment.setIsVerified(false);
        employmentService.updateEmployment(employment);
        return "redirect:/student/employment";
    }

    @PostMapping("/student/employment/delete/{id}")
    public String deleteEmployment(@PathVariable Long id) {
        Employment employment = employmentService.findEmploymentById(id);
        employmentService.deleteEmployment(employment);
        return "redirect:/student/employment";
    }

    //.................................... Courses ........................................

    @GetMapping("/student/courses")
    public String universityCourses(Model model) {
        UniversityApplication application = new UniversityApplication();
        model.addAttribute("application", application);
        return "student/courses";
    }

    @PostMapping("/student/courses/apply")
    public String universityCoursesApply(@RequestParam("majorId") Long majorId) {
        Major major = majorService.findById(majorId);
        UniversityApplication application = new UniversityApplication();
        application.setStatus(ApplicationStatus.SENT);
        application.setStudent(userAccountService.getCurrentUser());
        application.setMajor(major);
        universityApplicationService.save(application);
        return "redirect:/student/courses?" + "university=" + application.getMajor().getOrganization().getId();
    }

    @PostMapping("/student/courses/final")
    public String universityCoursesFinal(@RequestParam("majorId") Long majorId) {
        Major major = majorService.findById(majorId);
        UniversityApplication application = universityApplicationService.fetchByMajor(major, userAccountService.getCurrentUser());
        application.setStatus(ApplicationStatus.OFFER_ACCEPTED);
        universityApplicationService.save(application);

        return "redirect:/student/courses?" + "university=" + application.getMajor().getOrganization().getId();
    }

    //.................................... Jobs ........................................

    @GetMapping("/student/jobs")
    public String jobRoles(Model model) {
        JobApplications jobApplication = new JobApplications();
        model.addAttribute("jobApplication", jobApplication);
        return "student/jobs";
    }


    @PostMapping("/student/jobs/apply")
    public String employerJobsApply(@RequestParam("jobId") Long jobId) {
        JobRole jobRole = jobRoleService.findJobById(jobId);
        JobApplications application = new JobApplications();
        application.setStatus(ApplicationStatus.SENT);
        application.setStudent(userAccountService.getCurrentUser());
        application.setPosition(jobRole);
        jobApplicationsService.saveJobApplication(application);
        return "redirect:/student/jobs?" + "employer=" + jobRole.getEmployer().getId();
    }

    @PostMapping("/student/jobs/final")
    public String jobFinal(@RequestParam("jobId") Long jobId) {
        JobRole jobRole = jobRoleService.findJobById(jobId);
        JobApplications application = jobApplicationsService.fetchByJobRole(jobRole, userAccountService.getCurrentUser());
        application.setStatus(ApplicationStatus.OFFER_ACCEPTED);
        jobApplicationsService.saveJobApplication(application);

        return "redirect:/student/jobs?" + "employer=" + jobRole.getEmployer().getId();
    }
    @GetMapping("/student/documents")
    public String getDocuments(Model model) {
        try {
            UserAccount userAccount = userAccountService.getCurrentUser();
            List<StudentDocument> documents = studentDocumentService.getDocumentsForStudent(userAccount);
            model.addAttribute("documents", documents);
            return "student/documents";
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching documents: " + e.getMessage());
            return "error";
        }
    }

    //.................................... Documents ........................................

    @PostMapping("/student/documents/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("description") String description,
                                 @RequestParam("documentType") String documentType,
                                 Model model) {
        try {
            UserAccount userAccount = userAccountService.getCurrentUser();
            studentDocumentService.uploadFile(userAccount, file, description, documentType);
            return "redirect:/student/documents";
        } catch (Exception e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/student/documents/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            StudentDocument document = studentDocumentService.getDocumentById(id);

            Path filePath = Paths.get(document.getFilePath());
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists()) {
                throw new RuntimeException("File not found.");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @PostMapping("/student/documents/delete/{id}")
    public String deleteDocument(@PathVariable("id") Long id, Model model) {
        try {
            StudentDocument document = studentDocumentService.getDocumentById(id);
            if (document == null) {
                model.addAttribute("error", "Document not found!");
                return "error";
            }
            studentDocumentService.deleteDocument(document);
            return "redirect:/student/documents";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting document: " + e.getMessage());
            return "error";
        }
    }

}
