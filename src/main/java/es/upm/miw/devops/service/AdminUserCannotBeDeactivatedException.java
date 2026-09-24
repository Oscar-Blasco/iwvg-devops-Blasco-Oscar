package es.upm.miw.devops.service;

public class AdminUserCannotBeDeactivatedException extends RuntimeException {

    public AdminUserCannotBeDeactivatedException(String id) {
        super("Admin user cannot be deactivated: " + id);
    }
}
