package ar.credify.daoimpl;

import ar.credify.dao.UniversityApplicationDAO;
import ar.credify.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UniversityApplicationDAOImpl implements UniversityApplicationDAO {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void save(UniversityApplication universityApplication) {
        Session session = sessionFactory.getCurrentSession();
        if (universityApplication.getId() == null) {
            session.persist(universityApplication);
        } else {
            session.merge(universityApplication);
        }
    }

    @Override
    public UniversityApplication fetchByMajor(Major major, UserAccount user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM UniversityApplication WHERE major = :major and student = :student", UniversityApplication.class)
                .setParameter("major", major)
                .setParameter("student", user)
                .uniqueResult();
    }

    @Override
    public List<UniversityApplication> fetchAllByUniversity(Organization org) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM UniversityApplication ua WHERE ua.major.organization = :university", UniversityApplication.class)
                .setParameter("university", org)
                .getResultList();
    }

    @Override
    public UniversityApplication findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM UniversityApplication WHERE id = :id", UniversityApplication.class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
