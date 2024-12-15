package ar.credify.daoimpl;

import ar.credify.dao.OrganizationDAO;
import ar.credify.model.Organization;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class OrganizationDAOImpl implements OrganizationDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Organization organization) {
        Session session = sessionFactory.getCurrentSession();
        if (organization.getId() == null) {
            session.persist(organization);
        } else {
            session.merge(organization);
        }
    }

    @Override
    public Organization findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Organization.class, id);
    }

    @Override
    public Organization findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Organization WHERE name = :name", Organization.class)
                .setParameter("name", name)
                .uniqueResult();
    }


    @Override
    public Organization findOrganizationByUser(UserAccount user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT oe.organization FROM OrganizationEmployee oe WHERE oe.user = :user", Organization.class)
                .setParameter("user", user)
                .uniqueResult();
    }

    @Override
    public List<Organization> fetchUniversityList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Organization WHERE orgType = 'University'", Organization.class)
                .getResultList();
    }

    @Override
    public List<Organization> fetchEmployerList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Organization WHERE orgType = 'Employer'", Organization.class)
                .getResultList();
    }

}