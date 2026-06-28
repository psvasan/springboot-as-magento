package com.dualsession.vasan.app.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Retrieve the standard HTTP status code injected by the servlet container
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // If the resource does not match any controller method mapping
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", "The page you are looking for does not exist.");
                return "errors/404"; // Routes to src/main/resources/templates/errors/404.html
            }

            // Optional: Catch internal application crashes (500 errors)
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", "The page you are looking for does not exist.");
                return "errors/500";
            }
        }

        // Default generic error template fallback
        return "error";
    }
}
