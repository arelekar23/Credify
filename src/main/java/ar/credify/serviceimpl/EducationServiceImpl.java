package ar.credify.serviceimpl;

import ar.credify.dao.EducationDAO;
import ar.credify.model.Education;
import ar.credify.model.UserAccount;
import ar.credify.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationDAO educationDAO;
    @Override
    @Transactional
    public void save(Education education) {
        educationDAO.save(education);
    }

    @Override
    public List<Education> getEducationListForStudent(UserAccount user) {
        return educationDAO.getEducationListForStudent(user);
    }

    @Override
    public List<Education> findByStudentId(Long id) {
        return educationDAO.findByStudentId(id);
    }

    @Override
    public Education findById(Long id) {
        return educationDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteEducation(Education education) {
        educationDAO.deleteEducation(education);
    }

    @Override
    public List<Education> fetchEducationListForUniversity(Long universityId) {
        return educationDAO.fetchEducationListForUniversity(universityId);
    }
}
