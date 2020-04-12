package de.simpleprojectmanager.simpleprojectmanager.exception;

public class InvalidLastNameException extends UserCreateException{

    public InvalidLastNameException() {
        super("user.create.invalid.lastname");
    }
}
