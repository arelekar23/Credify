package ar.credify.serviceimpl;


import ar.credify.dao.EmploymentDAO;
import ar.credify.model.Employment;
import ar.credify.model.UserAccount;
import ar.credify.service.EmploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmploymentServiceImpl implements EmploymentService {
    @Autowired
    private EmploymentDAO employmentDAO;

    @Override
    @Transactional
    public void saveEmployment(Employment employment) {
        employmentDAO.save(employment);
    }

    @Override
    @Transactional
    public void updateEmployment(Employment employment) {
        employmentDAO.update(employment);
    }

    @Override
    @Transactional
    public void deleteEmployment(Employment employment) {
        employmentDAO.delete(employment);
    }

    @Override
    public Employment findEmploymentById(Long id) {
        return employmentDAO.findById(id);
    }

    @Override
    public List<Employment> getEmploymentListForStudent(UserAccount user) {
        return employmentDAO.getEmploymentListForStudent(user);
    }

    @Override
    public List<Employment> fetchEmploymentListForEmployer(Long employerId) {
        return employmentDAO.fetchEmploymentListForEmployer(employerId);
    }

    @Override
    public List<Employment> findByStudentId(Long id) {
        return employmentDAO.findByStudentId(id);
    }
}
