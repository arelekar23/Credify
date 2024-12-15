package ar.credify.dao;

import ar.credify.model.Education;
import ar.credify.model.Employment;
import ar.credify.model.UserAccount;

import java.util.List;

public interface EmploymentDAO {
    void save(Employment employment);
    void update(Employment employment);
    void delete(Employment employment);
    Employment findById(Long id);
    List<Employment> getEmploymentListForStudent(UserAccount user);
    List<Employment> fetchEmploymentListForEmployer(Long employerId);
    List<Employment> findByStudentId(Long id);
}
