package ar.credify.service;

import ar.credify.model.Employment;
import ar.credify.model.UserAccount;

import java.util.List;

public interface EmploymentService {
    void saveEmployment(Employment employment);
    void updateEmployment(Employment employment);
    void deleteEmployment(Employment employment);
    Employment findEmploymentById(Long id);
    List<Employment> getEmploymentListForStudent(UserAccount user);
    List<Employment> fetchEmploymentListForEmployer(Long employerId);
    List<Employment> findByStudentId(Long id);
}
