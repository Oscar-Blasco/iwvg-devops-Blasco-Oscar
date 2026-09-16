package es.upm.miw.devops.service;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super("User not found: " + id);
    }
}
