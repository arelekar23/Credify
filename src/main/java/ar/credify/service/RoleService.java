package ar.credify.service;

import ar.credify.model.Role;

public interface RoleService {
    Role getRoleByName(String name);
}