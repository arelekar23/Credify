package ar.credify.controller;

import ar.credify.model.JobApplications;
import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import ar.credify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployerRestController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private JobRoleService jobRoleService;
    @Autowired
    private JobApplicationsService jobApplicationsService;
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/employer/list")
    public List<Organization> employerList() {
        return organizationService.fetchEmployerList();
    }

    @GetMapping("/employer/jobroles/{selectedEmployer}")
    public List<JobRole> getJobRoles(@PathVariable Long selectedEmployer) {
        Organization org =  organizationService.findById(selectedEmployer);
        return jobRoleService.getJobListForOrg(org);
    }

    @GetMapping("/employer-applications/status/{jobId}")
    public ResponseEntity<String> fetchApplicationStatus(@PathVariable Long jobId) {
        JobRole jobRole = jobRoleService.findJobById(jobId);
        JobApplications application = jobApplicationsService.fetchByJobRole(jobRole, userAccountService.getCurrentUser());
        if (application != null) {
            return ResponseEntity.ok(application.getStatus().toString());
        }
        return ResponseEntity.ok("OPEN");
    }
}
