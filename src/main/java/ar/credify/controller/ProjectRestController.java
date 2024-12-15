package ar.credify.controller;

import ar.credify.model.*;
import ar.credify.service.OrganizationEmployeeService;
import ar.credify.service.ProjectService;
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectRestController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrganizationEmployeeService organizationEmployeeService;

    @PostMapping("/add")
    public String addNewProject(@RequestBody Project project) {
        project.setStudent(userAccountService.getCurrentUser());
        project.setIsVerified(false);
        projectService.save(project);
        return "Project added successfully";
    }

    @GetMapping("/list")
    public List<Project> fetchProjects() {
        UserAccount currentUser = userAccountService.getCurrentUser();
        return projectService.fetchProjectsByUser(currentUser);
    }

    @GetMapping("/academic/list")
    public List<Project> fetchAllAcademicProjectsByProfessor(@PathVariable String orgEmployeeId) {
        OrganizationEmployee orgEmployee = organizationEmployeeService.findById(Long.valueOf(orgEmployeeId));
        return projectService.fetchAllAcademicProjectsByProfessor(orgEmployee);
    }

    @PostMapping("/verify/{projectId}")
    public String verifyProject(@PathVariable String projectId) {
        Project project = projectService.findById(Long.valueOf(projectId));
        project.setIsVerified(true);
        projectService.save(project);
        return "Project verified successfully!";
    }
}
