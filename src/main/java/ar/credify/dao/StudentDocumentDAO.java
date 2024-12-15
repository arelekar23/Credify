package ar.credify.dao;

import ar.credify.model.StudentDocument;
import ar.credify.model.UserAccount;

import java.util.List;

public interface StudentDocumentDAO {
    void save(StudentDocument studentDocument);
    List<StudentDocument> findByStudent(UserAccount student);
    List<StudentDocument> findByStudentId(Long id);
    StudentDocument findById(Long id);
    void delete(StudentDocument document);
}
