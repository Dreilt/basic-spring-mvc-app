package pl.dreilt.basicspringmvcapp.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class CustomHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final MessageSource messageSource;

    public CustomHandlerExceptionResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof NoSuchRoleException) {
                return handleNoSuchRole((NoSuchRoleException) ex, response, handler);
            }
        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }

    private ModelAndView handleNoSuchRole(NoSuchRoleException ex, HttpServletResponse response, Object handler) {
        logger.error(String.format("Role with name %s not found", ex.getRoleName()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("register.registrationForm.NoSuchRoleException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }
}