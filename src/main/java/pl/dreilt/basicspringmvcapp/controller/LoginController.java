package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class LoginController {

    private final MessageSource messageSource;

    public LoginController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(required = false, defaultValue = "false") boolean error, HttpServletRequest request, Model model) {
        if (error) {
            AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            if (exception instanceof BadCredentialsException) {
                String errorMessage = messageSource.getMessage("login.loginForm.BadCredentialsException.message", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            } else if (exception instanceof DisabledException) {
                String errorMessage = messageSource.getMessage("login.loginForm.DisabledException.message", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            } else if (exception instanceof LockedException) {
                String errorMessage = messageSource.getMessage("login.loginForm.LockedException.message", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            } else {
                String errorMessage = messageSource.getMessage("login.loginForm.unknownError.message", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            }

            return "forms/login-form";
        }

        return "forms/login-form";
    }
}
