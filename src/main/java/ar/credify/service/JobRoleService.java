package ar.credify.service;

import ar.credify.model.JobRole;
import ar.credify.model.Organization;

import java.util.List;

public interface JobRoleService {
    void saveJob(JobRole job);
    void updateJob(JobRole job);
    void deleteJob(JobRole job);
    JobRole findJobById(Long id);
    List<JobRole> getJobListForOrg(Organization org);
    Organization findOrgByJobRoleId(Long jobRoleId);
}
