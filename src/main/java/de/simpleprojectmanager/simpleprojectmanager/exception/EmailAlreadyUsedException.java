package de.simpleprojectmanager.simpleprojectmanager.exception;

public class EmailAlreadyUsedException extends UserCreateException{
    public EmailAlreadyUsedException() {
        super("user.create.used.email");
    }
}
