package pl.dreilt.basicspringmvcapp.exception;

public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(String message) {
        super(message);
    }
}
