package ar.credify.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class RegistrationForm {
    @Valid
    private Person person;

    @Valid
    private UserAccount user;

    @NotEmpty
    private String role;

    @Valid
    private Organization organization;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}