package de.simpleprojectmanager.simpleprojectmanager.exception;

public class InvalidEmailException extends UserCreateException {

    public InvalidEmailException() {
        super("user.create.invalid.email");
    }
}
