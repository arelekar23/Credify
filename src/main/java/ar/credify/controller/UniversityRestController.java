package ar.credify.controller;

import ar.credify.model.Organization;
import ar.credify.model.OrganizationEmployee;
import ar.credify.model.Role;
import ar.credify.service.OrganizationEmployeeService;
import ar.credify.service.OrganizationService;
import ar.credify.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("api/university")
public class UniversityRestController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationEmployeeService organizationEmployeeService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public List<Organization> universitiesList() {
        return organizationService.fetchUniversityList();
    }

    @GetMapping("/users/by-role")
    public List<OrganizationEmployee> getUsersByRole(@RequestParam String role, @RequestParam long orgId ) {
        Organization currentOrg = organizationService.findById(orgId);
        Role userRole = roleService.getRoleByName(role);
        return organizationEmployeeService.fetchUsersByRole(currentOrg, userRole);
    }
}
