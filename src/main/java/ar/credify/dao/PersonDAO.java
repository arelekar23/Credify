package ar.credify.dao;

import ar.credify.model.Person;

public interface PersonDAO {
    void save(Person person);
    void delete(Person person);
    Person findByEmail(String email);
    Person findById(Long id);
    void update(Person person);
}