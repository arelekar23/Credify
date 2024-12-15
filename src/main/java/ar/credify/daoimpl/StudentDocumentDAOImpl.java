package ar.credify.daoimpl;

import ar.credify.dao.StudentDocumentDAO;
import ar.credify.model.StudentDocument;
import ar.credify.model.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StudentDocumentDAOImpl implements StudentDocumentDAO {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void save(StudentDocument studentDocument) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(studentDocument);
    }

    @Override
    public List<StudentDocument> findByStudent(UserAccount student) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM StudentDocument WHERE student = :student", StudentDocument.class)
                .setParameter("student", student)
                .getResultList();
    }

    @Override
    public List<StudentDocument> findByStudentId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM StudentDocument WHERE student.id = :id", StudentDocument.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public StudentDocument findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM StudentDocument WHERE id = :id", StudentDocument.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public void delete(StudentDocument document) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(document);
    }
}
