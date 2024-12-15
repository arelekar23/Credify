package ar.credify.dao;

import ar.credify.model.JobRole;
import ar.credify.model.Major;
import ar.credify.model.Organization;

import java.util.List;

public interface JobRoleDAO {
    void save(JobRole job);
    void update(JobRole job);
    void delete(JobRole job);
    JobRole findById(Long id);
    Organization findByJobRoleId(Long JobRoleId);
    List<JobRole> getJobListForOrg(Organization org);
}
