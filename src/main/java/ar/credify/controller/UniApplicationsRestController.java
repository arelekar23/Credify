package ar.credify.controller;

import ar.credify.model.Major;
import ar.credify.model.UniversityApplication;
import ar.credify.service.MajorService;
import ar.credify.service.UniversityApplicationService;
import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/university-applications")
public class UniApplicationsRestController {
    @Autowired
    private UniversityApplicationService universityApplicationService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private MajorService majorService;

    @GetMapping("/status/{majorId}")
    public ResponseEntity<String> fetchApplicationStatus(@PathVariable Long majorId) {
        Major major = majorService.findById(majorId);
        System.out.println(major.getId());
        UniversityApplication application = universityApplicationService.fetchByMajor(major, userAccountService.getCurrentUser());

        if (application != null) {
            return ResponseEntity.ok(application.getStatus().toString());
        }
        return ResponseEntity.ok("OPEN");
    }
}
