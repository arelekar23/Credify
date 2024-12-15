package ar.credify.dao;

import ar.credify.model.Education;
import ar.credify.model.UserAccount;
import java.util.List;

public interface EducationDAO {
    void save(Education education);
    List<Education> getEducationListForStudent(UserAccount user);
    Education findById(Long id);
    void deleteEducation(Education education);
    List<Education> fetchEducationListForUniversity(Long universityId);
    List<Education> findByStudentId(Long id);
}
