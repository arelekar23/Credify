package ar.credify.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String message = "An unexpected error occurred.";
        System.out.println(errorMessage);
        System.out.println(status);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            switch (statusCode) {
                case 404:
                    message = "Oops! We couldn't find what you were looking for.";
                    break;
                case 500:
                    message = "Something went wrong on our end! We'll fix it soon.";
                    break;
                case 403:
                    message = "You don't have permission to access this page.";
                    break;
                case 413:
                    message = "The file you're uploading is too large. Try a smaller file.";
                    break;
                default:
                    message = "Something went wrong! We're looking into it.";
            }
        }
        model.addAttribute("statusCode", status);
        model.addAttribute("message", message);
        return "error";
    }
}
