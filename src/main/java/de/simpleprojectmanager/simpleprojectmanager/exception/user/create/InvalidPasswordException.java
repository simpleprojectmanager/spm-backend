package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class InvalidPasswordException extends UserCreateException{
    public InvalidPasswordException() {
        super("user.create.invalid.password");
    }
}
