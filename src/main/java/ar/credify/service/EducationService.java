package ar.credify.service;

import ar.credify.model.Education;
import ar.credify.model.Project;
import ar.credify.model.UserAccount;

import java.util.List;

public interface EducationService {
    void save(Education education);
    List<Education> getEducationListForStudent(UserAccount user);
    List<Education> findByStudentId(Long id);
    Education findById(Long id);
    void deleteEducation(Education education);
    List<Education> fetchEducationListForUniversity(Long universityId);
}
