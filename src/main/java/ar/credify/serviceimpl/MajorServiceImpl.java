package ar.credify.serviceimpl;

import ar.credify.dao.MajorDAO;
import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorDAO majorDAO;

    @Override
    @Transactional
    public void save(Major major) {
        majorDAO.save(major);
    }

    @Override
    public List<Major> getCourseListForOrg(Organization org) {
        return majorDAO.getCourseListForOrg(org);
    }

    @Override
    public Major findById(Long id) {
        return majorDAO.findById(id);
    }

    @Override
    public Organization findOrgByMajorId(Long majorId) {
        return majorDAO.findByMajorId(majorId);
    }

    @Override
    @Transactional
    public void deleteCourse(Major major) {
        majorDAO.deleteCourse(major);
    }

    @Override
    public List<Major> findByUniversity(Organization org) {
        return majorDAO.findByUniversity(org);
    }
}
