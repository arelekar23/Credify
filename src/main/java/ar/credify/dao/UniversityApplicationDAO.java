package ar.credify.dao;

import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.model.UniversityApplication;
import ar.credify.model.UserAccount;

import java.util.List;

public interface UniversityApplicationDAO {
    void save(UniversityApplication universityApplication);
    UniversityApplication fetchByMajor(Major major, UserAccount user);
    List<UniversityApplication> fetchAllByUniversity(Organization org);
    UniversityApplication findById(Long id);
}
