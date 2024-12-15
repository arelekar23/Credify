package ar.credify.dao;

import ar.credify.model.*;

import java.util.List;
import java.util.Map;

public interface JobApplicationsDAO {
    void save(JobApplications jobApplications);
    void update(JobApplications jobApplications);
    void delete(JobApplications jobApplications);
    JobApplications findById(Long id);
    JobApplications fetchByJobRole(JobRole jobRole, UserAccount user);
    List<JobApplications> fetchApplicationsByUniversity(Organization org);
}
