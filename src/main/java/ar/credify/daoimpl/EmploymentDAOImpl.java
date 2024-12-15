package ar.credify.daoimpl;

import ar.credify.dao.EmploymentDAO;
import ar.credify.model.Employment;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmploymentDAOImpl implements EmploymentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Employment employment) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(employment);
    }

    @Override
    public void update(Employment employment) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(employment);
    }

    @Override
    public void delete(Employment employment) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(employment);
    }

    @Override
    public Employment findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Employment WHERE id = :id", Employment.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Employment> getEmploymentListForStudent(UserAccount user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Employment WHERE user = :student", Employment.class)
                .setParameter("student", user)
                .getResultList();
    }

    @Override
    public List<Employment> fetchEmploymentListForEmployer(Long employerId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Employment WHERE position.employer.id = :employerId", Employment.class)
                .setParameter("employerId", employerId)
                .getResultList();
    }

    @Override
    public List<Employment> findByStudentId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Employment WHERE user.id = :id", Employment.class)
                .setParameter("id", id)
                .getResultList();
    }
}
