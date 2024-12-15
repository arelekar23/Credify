package ar.credify.dao;

import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.model.UserAccount;

import java.util.List;

public interface OrganizationDAO {
    void save(Organization organization);
    Organization findById(Long id);
    Organization findByName(String name);
    Organization findOrganizationByUser(UserAccount user);
    List<Organization> fetchUniversityList();
    List<Organization> fetchEmployerList();
}