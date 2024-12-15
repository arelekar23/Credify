package ar.credify.service;

import ar.credify.model.Person;

public interface PersonService {
    void registerPerson(Person person);
    void deletePerson(Person person);
    Person getPersonByEmail(String email);
    void updatePerson(Person person);
    Person findById(Long id);
}