package ar.credify.dao;

import ar.credify.model.Major;
import ar.credify.model.Organization;

import java.util.List;

public interface MajorDAO {
    void save(Major major);
    List<Major> getCourseListForOrg(Organization org);
    Organization findByMajorId(Long majorId);
    Major findById(Long id);
    void deleteCourse(Major major);
    List<Major> findByUniversity(Organization org);
}
