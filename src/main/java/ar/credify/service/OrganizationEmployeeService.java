package ar.credify.service;

import ar.credify.model.Organization;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Role;
import ar.credify.model.UserAccount;

import java.util.List;

public interface OrganizationEmployeeService {
    void save(OrganizationEmployee organizationEmployee);
    void deleteUser(OrganizationEmployee organizationEmployee);
    OrganizationEmployee findById(Long id);
    List<OrganizationEmployee> fetchUsers(Organization org);
    List<OrganizationEmployee> fetchUsersByRole(Organization currentOrg, Role role);
    OrganizationEmployee findByUserId(UserAccount user);
}
