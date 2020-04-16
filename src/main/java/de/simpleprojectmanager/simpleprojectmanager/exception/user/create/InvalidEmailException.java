package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class InvalidEmailException extends UserCreateException {

    public InvalidEmailException() {
        super("user.create.invalid.email");
    }
}
