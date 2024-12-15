package ar.credify.serviceimpl;

import ar.credify.dao.ProjectDAO;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Project;
import ar.credify.model.UserAccount;
import ar.credify.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDAO projectDAO;

    @Override
    @Transactional
    public void save(Project project) {
        projectDAO.save(project);
    }

    @Override
    @Transactional
    public void deleteProject(Project project) {
        projectDAO.delete(project);
    }

    @Override
    public List<Project> fetchProjectsByUser(UserAccount user) {
        return projectDAO.fetchProjectsByUser(user);
    }

    @Override
    public List<Project> fetchAllAcademicProjectsByProfessor(OrganizationEmployee orgEmployee) {
        return projectDAO.fetchAllAcademicProjectsByProfessor(orgEmployee);
    }

    @Override
    public Project findById(Long id) {
        return projectDAO.findById(id);
    }

    @Override
    public List<Project> findByStudentId(Long id) {
        return projectDAO.findByStudentId(id);
    }
}
