package ar.credify.serviceimpl;

import ar.credify.dao.JobApplicationsDAO;
import ar.credify.model.JobApplications;
import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import ar.credify.model.UserAccount;
import ar.credify.service.JobApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class JobApplicationsServiceImpl implements JobApplicationsService {
    @Autowired
    private JobApplicationsDAO jobApplicationsDAO;
    @Override
    @Transactional
    public void saveJobApplication(JobApplications jobApplication) {
        jobApplicationsDAO.save(jobApplication);
    }

    @Override
    @Transactional
    public void updateJobApplication(JobApplications jobApplication) {
        jobApplicationsDAO.update(jobApplication);
    }

    @Override
    @Transactional
    public void deleteJobApplication(JobApplications jobApplication) {
        jobApplicationsDAO.delete(jobApplication);
    }

    @Override
    public JobApplications findJobApplicationById(Long id) {
        return jobApplicationsDAO.findById(id);
    }

    @Override
    public JobApplications fetchByJobRole(JobRole jobRole, UserAccount user) {
        return jobApplicationsDAO.fetchByJobRole(jobRole, user);
    }

    @Override
    public List<JobApplications> fetchApplicationsByUniversity(Organization org) {
        return jobApplicationsDAO.fetchApplicationsByUniversity(org);
    }
}
