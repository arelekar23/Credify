package ar.credify.daoimpl;

import ar.credify.dao.JobApplicationsDAO;
import ar.credify.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JobApplicationsDAOImpl implements JobApplicationsDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(JobApplications jobApplications) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(jobApplications);
    }

    @Override
    public void update(JobApplications jobApplications) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(jobApplications);
    }

    @Override
    public void delete(JobApplications jobApplications) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(jobApplications);
    }

    @Override
    public JobApplications findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM JobApplications WHERE id = :id", JobApplications.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public JobApplications fetchByJobRole(JobRole jobRole, UserAccount user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM JobApplications WHERE position = :jobRole and student = :student", JobApplications.class)
                .setParameter("jobRole", jobRole)
                .setParameter("student", user)
                .uniqueResult();
    }

    @Override
    public List<JobApplications> fetchApplicationsByUniversity(Organization org) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM JobApplications WHERE position.employer = :org", JobApplications.class)
                .setParameter("org", org)
                .getResultList();
    }
}
