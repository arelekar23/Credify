package ar.credify.model;

import jakarta.persistence.*;

@Entity
@Table(name = "OrganizationEmployee")
public class OrganizationEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    public OrganizationEmployee() {}

    public OrganizationEmployee(Organization organization, UserAccount user) {
        this.organization = organization;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrganizationEmployee{" +
                "id=" + id +
                ", organization=" + organization.getName() +
                ", user=" + user.getUsername() +
                '}';
    }
}