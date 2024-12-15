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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/studentDetails")
public class StudentDetailsViewController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private EmploymentService employmentService;

    @Autowired
    private StudentDocumentService documentService;

    @Autowired
    private UniversityApplicationService universityApplicationService;
    @Autowired
    private JobApplicationsService jobApplicationsService;

    @GetMapping("/view/{studentId}")

    public String viewStudentDetails(@PathVariable Long studentId, Model model) {
        Person student = userAccountService.findUserById(studentId).getPerson();

        List<Project> projects = projectService.findByStudentId(studentId);

        List<Education> educations = educationService.findByStudentId(studentId);

        List<Employment> employments = employmentService.findByStudentId(studentId);

        List<StudentDocument> documents = documentService.getDocumentsForStudent(userAccountService.findUserById(studentId));

        model.addAttribute("student", student);
        model.addAttribute("projects", projects);
        model.addAttribute("educations", educations);
        model.addAttribute("employments", employments);
        model.addAttribute("documents", documents);

        return "studentDetails";
    }

    @PostMapping("/updateStatus/{appId}")
    public String updateStatus(@PathVariable Long appId, Model model) {
        UniversityApplication app = universityApplicationService.findById(appId);
        universityApplicationService.save(app);

        return "redirect:/studentDetails/view/" + app.getStudent().getId();
    }

    @PostMapping("/updateJobAppStatus/{appId}")
    public String updateJobAppStatus(@PathVariable Long appId, Model model) {
        JobApplications app = jobApplicationsService.findJobApplicationById(appId);
        jobApplicationsService.saveJobApplication(app);

        return "redirect:/studentDetails/view/" + app.getStudent().getId();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            StudentDocument document = documentService.getDocumentById(id);

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
}
