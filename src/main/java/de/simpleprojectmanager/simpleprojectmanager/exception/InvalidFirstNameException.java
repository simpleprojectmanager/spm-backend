package de.simpleprojectmanager.simpleprojectmanager.exception;

public class InvalidFirstNameException extends UserCreateException{

    public InvalidFirstNameException() {
        super("user.create.invalid.firstname");
    }
}
