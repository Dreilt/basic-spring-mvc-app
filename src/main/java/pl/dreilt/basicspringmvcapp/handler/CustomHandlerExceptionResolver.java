package pl.dreilt.basicspringmvcapp.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import pl.dreilt.basicspringmvcapp.exception.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class CustomHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    protected final Log log = LogFactory.getLog(this.getClass());
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
            if (ex instanceof AppUserNotFoundException) {
                return handleAppUserNotFound((AppUserNotFoundException) ex, response, handler);
            }
            if (ex instanceof DefaultProfileImageNotFoundException) {
                return handleDefaultProfileImageNotFound((DefaultProfileImageNotFoundException) ex, response, handler);
            }
            if (ex instanceof DefaultEventImageNotFoundException) {
                return handleDefaultEventImageNotFound((DefaultEventImageNotFoundException) ex, response, handler);
            }
            if (ex instanceof EventNotFoundException) {
                return handleEventNotFound((EventNotFoundException) ex, response, handler);
            }
        } catch (Exception handlerException) {
            log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }

    private ModelAndView handleNoSuchRole(NoSuchRoleException ex, HttpServletResponse response, Object handler) {
        log.error(String.format("Role with name %s not found", ex.getRoleName()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("registrationForm.exception.NoSuchRoleException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private ModelAndView handleAppUserNotFound(AppUserNotFoundException ex, HttpServletResponse response, Object handler) {
        log.error(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("exception.AppUserNotFoundException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private ModelAndView handleDefaultProfileImageNotFound(DefaultProfileImageNotFoundException ex, HttpServletResponse response, Object handler) {
        log.error(ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("registrationForm.exception.DefaultProfileImageNotFoundException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private ModelAndView handleEventNotFound(EventNotFoundException ex, HttpServletResponse response, Object handler) {
        log.error(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("exception.EventNotFoundException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private ModelAndView handleDefaultEventImageNotFound(DefaultEventImageNotFoundException ex, HttpServletResponse response, Object handler) {
        log.error(ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("exception.DefaultEventImageNotFoundException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
