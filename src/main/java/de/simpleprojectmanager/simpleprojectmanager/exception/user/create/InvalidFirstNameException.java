package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class InvalidFirstNameException extends UserCreateException{

    public InvalidFirstNameException() {
        super("user.create.invalid.firstname");
    }
}
