package ar.credify.daoimpl;

import ar.credify.dao.MajorDAO;
import ar.credify.model.Major;
import ar.credify.model.Organization;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MajorDAOImpl implements MajorDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Major major) {
        Session session = sessionFactory.getCurrentSession();
        if (major.getId() == null) {
            session.persist(major);
        } else {
            session.merge(major);
        }
    }

    @Override
    public List<Major> getCourseListForOrg(Organization org) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Major WHERE organization = :org", Major.class)
                .setParameter("org", org)
                .getResultList();
    }

    @Override
    public Organization findByMajorId(Long majorId) {
        try {
            String hql = "SELECT m.organization FROM Major m WHERE m.id = :majorId";
            return (Organization) sessionFactory.getCurrentSession().createQuery(hql)
                    .setParameter("majorId", majorId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public Major findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Major WHERE id = :id", Major.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public void deleteCourse(Major major) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(major);
    }

    @Override
    public List<Major> findByUniversity(Organization org) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Major WHERE organization = :org", Major.class)
                .setParameter("org", org)
                .getResultList();
    }
}
