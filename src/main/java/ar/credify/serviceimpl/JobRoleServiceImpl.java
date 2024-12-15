package ar.credify.serviceimpl;

import ar.credify.dao.JobRoleDAO;
import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import ar.credify.service.JobRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobRoleServiceImpl implements JobRoleService {
    @Autowired
    private JobRoleDAO jobRoleDAO;

    @Override
    @Transactional
    public void saveJob(JobRole job) {
        jobRoleDAO.save(job);
    }

    @Override
    @Transactional
    public void updateJob(JobRole job) {
        jobRoleDAO.update(job);
    }

    @Override
    @Transactional
    public void deleteJob(JobRole job) {
        jobRoleDAO.delete(job);
    }

    @Override
    public JobRole findJobById(Long id) {
        return jobRoleDAO.findById(id);
    }

    @Override
    public List<JobRole> getJobListForOrg(Organization org) {
        return jobRoleDAO.getJobListForOrg(org);
    }

    @Override
    public Organization findOrgByJobRoleId(Long jobRoleId) {
        return jobRoleDAO.findByJobRoleId(jobRoleId);
    }
}
