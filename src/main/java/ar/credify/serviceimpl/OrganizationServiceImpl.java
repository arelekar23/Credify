package ar.credify.serviceimpl;

import ar.credify.dao.OrganizationDAO;
import ar.credify.model.Organization;
import ar.credify.model.UserAccount;
import ar.credify.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDAO organizationDAO;

    @Override
    @Transactional
    public void save(Organization organization) {
        organizationDAO.save(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return organizationDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByName(String name) {
        return organizationDAO.findByName(name);
    }
    @Override
    public Organization findOrganizationByUser(UserAccount user) {
        return organizationDAO.findOrganizationByUser(user);
    }

    @Override
    public List<Organization> fetchUniversityList() {
        return organizationDAO.fetchUniversityList();
    }

    @Override
    public List<Organization> fetchEmployerList() {
        return organizationDAO.fetchEmployerList();
    }
}