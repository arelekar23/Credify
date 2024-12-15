package ar.credify.serviceimpl;

import ar.credify.model.Organization;
import ar.credify.model.OrganizationEmployee;
import ar.credify.dao.OrganizationEmployeeDAO;
import ar.credify.model.Role;
import ar.credify.model.UserAccount;
import ar.credify.service.OrganizationEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationEmployeeServiceImpl implements OrganizationEmployeeService {

    @Autowired
    private OrganizationEmployeeDAO organizationEmployeeDAO;

    @Override
    @Transactional
    public void save(OrganizationEmployee organizationEmployee) {
        organizationEmployeeDAO.save(organizationEmployee);
    }

    @Override
    @Transactional
    public void deleteUser(OrganizationEmployee organizationEmployee) {
        organizationEmployeeDAO.delete(organizationEmployee);
    }

    @Override
    @Transactional
    public OrganizationEmployee findById(Long id) {
        return organizationEmployeeDAO.findById(id);
    }

    @Override
    @Transactional
    public List<OrganizationEmployee> fetchUsers(Organization org) {
        return organizationEmployeeDAO.fetchUsers(org);
    }

    @Override
    public List<OrganizationEmployee> fetchUsersByRole(Organization currentOrg, Role role) {
        return organizationEmployeeDAO.fetchUsersByRole(currentOrg, role);
    }

    @Override
    public OrganizationEmployee findByUserId(UserAccount user) {
        return organizationEmployeeDAO.findByUserId(user);
    }
}