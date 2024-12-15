package ar.credify.dao;

import ar.credify.model.Role;

public interface RoleDAO {
    Role findByName(String name);
}