package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.dreilt.basicspringmvcapp.dto.LoggedAppUserBasicDataDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

@ControllerAdvice
public class LoggedAppUserBasicDataController {

    private final AppUserService appUserService;

    public LoggedAppUserBasicDataController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @ModelAttribute(name = "loggedUserBasicData")
    public LoggedAppUserBasicDataDto getLoggedUserBasicData() {
        LoggedAppUserBasicDataDto loggedUserBasicData = null;
        if (isAppUserLogged()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            loggedUserBasicData = appUserService.findLoggedUserBasicDataByUsername(username);
        }

        return loggedUserBasicData;
    }

    public static boolean isAppUserLogged() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousUser")) {
            return false;
        }

        return true;
    }
}
