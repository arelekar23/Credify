package ar.credify.daoimpl;

import ar.credify.dao.OrganizationEmployeeDAO;
import ar.credify.model.Organization;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Role;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class OrganizationEmployeeDAOImpl implements OrganizationEmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(OrganizationEmployee organizationEmployee) {
        Session session = sessionFactory.getCurrentSession();
        if (organizationEmployee.getId() == null) {
            session.persist(organizationEmployee);
        } else {
            session.merge(organizationEmployee);
        }
    }

    @Override
    public void delete(OrganizationEmployee organizationEmployee) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(organizationEmployee);
    }

    @Override
    public OrganizationEmployee findById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM OrganizationEmployee WHERE id = :id", OrganizationEmployee.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public OrganizationEmployee findByUserId(UserAccount user) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM OrganizationEmployee WHERE user = :user", OrganizationEmployee.class)
                .setParameter("user", user)
                .uniqueResult();
    }

    @Override
    public List<OrganizationEmployee> fetchUsers(Organization org) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM OrganizationEmployee WHERE organization = :org", OrganizationEmployee.class)
                .setParameter("org", org)
                .getResultList();
    }

    @Override
    public List<OrganizationEmployee> fetchUsersByRole(Organization currentOrg, Role role) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM OrganizationEmployee oe WHERE oe.organization = :org AND oe.user.role = :role",
                        OrganizationEmployee.class
                )
                .setParameter("org", currentOrg)
                .setParameter("role", role)
                .getResultList();
    }
}

