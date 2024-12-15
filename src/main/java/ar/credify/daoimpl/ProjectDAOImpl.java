package ar.credify.daoimpl;

import ar.credify.dao.ProjectDAO;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Project;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Project project) {
        Session session = sessionFactory.getCurrentSession();
        if (project.getId() == null) {
            session.persist(project);
        } else {
            session.merge(project);
        }
    }

    @Override
    public void delete(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(project);
    }

    @Override
    public List<Project> fetchProjectsByUser(UserAccount user) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Project WHERE student = :user", Project.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Project> fetchAllAcademicProjectsByProfessor(OrganizationEmployee orgEmployee) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Project WHERE professor = :prof", Project.class)
                .setParameter("prof", orgEmployee)
                .getResultList();
    }

    @Override
    public Project findById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Project WHERE id = :id", Project.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Project> findByStudentId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Project WHERE student.id = :id", Project.class)
                .setParameter("id", id)
                .getResultList();
    }
}
