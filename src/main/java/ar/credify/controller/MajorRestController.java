package ar.credify.controller;

import ar.credify.model.Major;
import ar.credify.model.Organization;
import ar.credify.service.MajorService;
import ar.credify.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorRestController {

    @Autowired
    private MajorService majorService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/university/{universityId}")
    public List<Major> getMajorsByUniversity(@PathVariable Long universityId) {
        Organization org = organizationService.findById(universityId);
        return majorService.findByUniversity(org);
    }
}