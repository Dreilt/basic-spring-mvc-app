package pl.dreilt.basicspringmvcapp.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = "/login?error";
        try {
            if (exception instanceof BadCredentialsException) {
                redirectUrl = "/login?error=badCredentials";
            } else if (exception instanceof DisabledException) {
                redirectUrl = "/login?error=disabledAccount";
            } else if (exception instanceof LockedException) {
                redirectUrl = "/login?error=lockedAccount";
            }
        } catch (Exception otherException) {
            redirectUrl = "/login?error=unknownError";
            logger.warn("Handling of [" + exception.getClass().getName() + "] resulted in Exception", otherException);
        }

        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
}
