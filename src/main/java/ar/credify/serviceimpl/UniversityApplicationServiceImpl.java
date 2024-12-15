package ar.credify.serviceimpl;

import ar.credify.dao.UniversityApplicationDAO;
import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.model.UniversityApplication;
import ar.credify.model.UserAccount;
import ar.credify.service.UniversityApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UniversityApplicationServiceImpl implements UniversityApplicationService {
    @Autowired
    private UniversityApplicationDAO universityApplicationDAO;

    @Override
    @Transactional
    public void save(UniversityApplication universityApplication) {
        universityApplicationDAO.save(universityApplication);
    }

    @Override
    public UniversityApplication fetchByMajor(Major major, UserAccount user) {
        return universityApplicationDAO.fetchByMajor(major, user);
    }

    @Override
    public List<UniversityApplication> fetchAllByUniversity(Organization org) {
        return universityApplicationDAO.fetchAllByUniversity(org);
    }

    @Override
    public UniversityApplication findById(Long id) {
        return universityApplicationDAO.findById(id);
    }
}
