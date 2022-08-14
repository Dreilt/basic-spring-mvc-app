package pl.dreilt.basicspringmvcapp.exception;

public class NoSuchRoleException extends RuntimeException {

    private final String roleName;

    public NoSuchRoleException(String message, String roleName) {
        super(message);
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
