package ar.credify.serviceimpl;

import ar.credify.dao.RoleDAO;
import ar.credify.model.Role;
import ar.credify.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDAO.findByName(name);
    }
}