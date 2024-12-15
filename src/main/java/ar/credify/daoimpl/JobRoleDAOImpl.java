package ar.credify.daoimpl;

import ar.credify.dao.JobRoleDAO;
import ar.credify.model.JobRole;
import ar.credify.model.Organization;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JobRoleDAOImpl implements JobRoleDAO {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void save(JobRole job) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(job);
    }

    @Override
    public void update(JobRole job) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(job);
    }

    @Override
    public void delete(JobRole job) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(job);
    }

    @Override
    public JobRole findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM JobRole WHERE id = :id", JobRole.class)
                .setParameter("id", id)
                .uniqueResult();
    }
    @Override
    public Organization findByJobRoleId(Long JobRoleId) {
        try {
            String hql = "SELECT j.employer FROM JobRole j WHERE j.id = :JobRoleId";
            return (Organization) sessionFactory.getCurrentSession().createQuery(hql)
                    .setParameter("JobRoleId", JobRoleId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<JobRole> getJobListForOrg(Organization org) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM JobRole WHERE employer = :org", JobRole.class)
                .setParameter("org", org)
                .getResultList();
    }
}
