package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class EmailAlreadyUsedException extends UserCreateException{
    public EmailAlreadyUsedException() {
        super("user.create.used.email");
    }
}
