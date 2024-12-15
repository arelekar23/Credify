package ar.credify.dao;

import ar.credify.model.Organization;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Role;
import ar.credify.model.UserAccount;

import java.util.List;

public interface OrganizationEmployeeDAO {
    void save(OrganizationEmployee organizationEmployee);
    void delete(OrganizationEmployee organizationEmployee);
    OrganizationEmployee findById(Long id);
    OrganizationEmployee findByUserId(UserAccount user);
    List<OrganizationEmployee> fetchUsers(Organization org);
    List<OrganizationEmployee> fetchUsersByRole(Organization currentOrg, Role role);
}