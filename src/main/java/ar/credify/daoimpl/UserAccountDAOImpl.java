package ar.credify.daoimpl;

import ar.credify.dao.UserAccountDAO;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserAccountDAOImpl implements UserAccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(UserAccount userAccount) {
        Session session = sessionFactory.getCurrentSession();
        if (userAccount.getId() == null) {
            session.persist(userAccount);
        } else {
            session.merge(userAccount);
        }
    }

    @Override
    public void delete(UserAccount userAccount) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(userAccount);
    }

    @Override
    public UserAccount findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UserAccount WHERE username = :username", UserAccount.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public UserAccount findUserById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UserAccount WHERE id = :id", UserAccount.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<UserAccount> fetchAllUsers() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UserAccount", UserAccount.class)
                .getResultList();
    }
}