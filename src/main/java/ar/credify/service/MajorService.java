package ar.credify.service;

import ar.credify.model.Major;
import ar.credify.model.Organization;

import java.util.List;

public interface MajorService {
    void save(Major major);
    List<Major> getCourseListForOrg(Organization org);
    Major findById(Long id);
    Organization findOrgByMajorId(Long majorId);
    void deleteCourse(Major major);
    List<Major> findByUniversity(Organization org);
}
