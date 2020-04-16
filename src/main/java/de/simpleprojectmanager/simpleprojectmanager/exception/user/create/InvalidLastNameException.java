package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class InvalidLastNameException extends UserCreateException{

    public InvalidLastNameException() {
        super("user.create.invalid.lastname");
    }
}
