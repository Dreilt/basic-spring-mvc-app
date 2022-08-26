package pl.dreilt.basicspringmvcapp.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = getRedirectUrl(exception);
        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }

    private String getRedirectUrl(AuthenticationException exception) {
        return switch (exception) {
            case BadCredentialsException badCredentialsException -> "/login?error=badCredentials";
            case DisabledException disabledException -> "/login?error=disabledAccount";
            case LockedException lockedException -> "/login?error=lockedAccount";
            default -> "/login?error=unknownError";
        };
    }
}
