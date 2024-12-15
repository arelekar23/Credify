package ar.credify.serviceimpl;

import ar.credify.dao.PersonDAO;
import ar.credify.model.Person;
import ar.credify.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDAO personDAO;

    @Override
    @Transactional
    public void registerPerson(Person person) {
        personDAO.save(person);
    }

    @Override
    @Transactional
    public void deletePerson(Person person) {
        personDAO.delete(person);
    }

    @Override
    public Person getPersonByEmail(String email) {
        return personDAO.findByEmail(email);
    }

    @Override
    @Transactional
    public void updatePerson(Person person) {
        personDAO.update(person);
    }

    @Override
    public Person findById(Long id) {
        return personDAO.findById(id);
    }
}