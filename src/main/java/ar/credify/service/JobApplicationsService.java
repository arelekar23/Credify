package ar.credify.service;

import ar.credify.model.JobApplications;
import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import ar.credify.model.UserAccount;

import java.util.List;
import java.util.Map;

public interface JobApplicationsService {
    void saveJobApplication(JobApplications jobApplications);
    void updateJobApplication(JobApplications jobApplications);
    void deleteJobApplication(JobApplications jobApplications);
    JobApplications findJobApplicationById(Long id);
    JobApplications fetchByJobRole(JobRole jobRole, UserAccount user);

    List<JobApplications> fetchApplicationsByUniversity(Organization org);
}
