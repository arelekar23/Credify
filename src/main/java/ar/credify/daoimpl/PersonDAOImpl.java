package ar.credify.daoimpl;

import ar.credify.dao.PersonDAO;
import ar.credify.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDAOImpl implements PersonDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(person);
    }

    @Override
    public void delete(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(person);
    }

    @Override
    public Person findByEmail(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Person WHERE emailAddress = :email", Person.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public Person findById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Person WHERE id = :id", Person.class)
                .setParameter("id", id)
                .uniqueResult();
    }


    @Override
    public void update(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(person);
    }
}