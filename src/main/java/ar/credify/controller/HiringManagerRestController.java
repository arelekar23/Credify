package ar.credify.controller;

import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import ar.credify.service.JobRoleService;
import ar.credify.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class HiringManagerRestController {
    @Autowired
    private JobRoleService jobRoleService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/employer/{employerId}")
    public List<JobRole> getMajorsByUniversity(@PathVariable Long employerId) {
        Organization org = organizationService.findById(employerId);
        return jobRoleService.getJobListForOrg(org);
    }
}
