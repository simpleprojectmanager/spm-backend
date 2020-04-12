package de.simpleprojectmanager.simpleprojectmanager.exception;

public class InvalidPasswordException extends UserCreateException{
    public InvalidPasswordException() {
        super("user.create.invalid.password");
    }
}
