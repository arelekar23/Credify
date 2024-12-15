package ar.credify.daoimpl;

import ar.credify.dao.EducationDAO;
import ar.credify.model.Education;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EducationDAOImpl implements EducationDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Education education) {
        Session session = sessionFactory.getCurrentSession();
        if (education.getId() == null) {
            session.persist(education);
        } else {
            session.merge(education);
        }
    }

    @Override
    public List<Education> getEducationListForStudent(UserAccount user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Education WHERE student = :student", Education.class)
                .setParameter("student", user)
                .getResultList();
    }

    @Override
    public Education findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Education WHERE id = :id", Education.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public void deleteEducation(Education education) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(education);
    }

    @Override
    public List<Education> fetchEducationListForUniversity(Long universityId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Education e WHERE  e.major.organization.id = :universityId", Education.class)
                .setParameter("universityId", universityId)
                .getResultList();
    }

    @Override
    public List<Education> findByStudentId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Education WHERE student.id = :id", Education.class)
                .setParameter("id", id)
                .getResultList();
    }

}
