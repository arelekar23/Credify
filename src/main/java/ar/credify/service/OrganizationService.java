package ar.credify.service;

import ar.credify.model.Organization;
import ar.credify.model.UserAccount;

import java.util.List;

public interface OrganizationService {
    void save(Organization organization);
    Organization findById(Long id);
    Organization findByName(String name);
    Organization findOrganizationByUser(UserAccount user);
    List<Organization> fetchUniversityList();
    List<Organization> fetchEmployerList();
}
