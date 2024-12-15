package ar.credify.dao;

import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Project;
import ar.credify.model.UserAccount;

import java.util.List;

public interface ProjectDAO {
    void save(Project project);
    void delete(Project project);
    List<Project> fetchProjectsByUser(UserAccount user);
    List<Project> fetchAllAcademicProjectsByProfessor(OrganizationEmployee orgEmployee);
    Project findById(Long id);
    List<Project> findByStudentId(Long id);

}
