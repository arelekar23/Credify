package ar.credify.controller;

import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {
    @Autowired
    private UserAccountService userAccountService;
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "student/dashboard";
    }

    @GetMapping("/university/dashboard")
    public String universityDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "university/dashboard";
    }

    @GetMapping("/professor/dashboard")
    public String professorDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "professor/dashboard";
    }

    @GetMapping("/registrar/dashboard")
    public String registarDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "registrar/dashboard";
    }

    @GetMapping("/admissionCommittee/dashboard")
    public String admissionCommitteeDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "admissionCommittee/dashboard";
    }

    @GetMapping("/employer/dashboard")
    public String employerDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "employer/dashboard";
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "recruiter/dashboard";
    }

    @GetMapping("/hiringManager/dashboard")
    public String hiringManagerDashboard(Model model) {
        model.addAttribute("user", userAccountService.getCurrentUser());
        return "hiringManager/dashboard";
    }
}
