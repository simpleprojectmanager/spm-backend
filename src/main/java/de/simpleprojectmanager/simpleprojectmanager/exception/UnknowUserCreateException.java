package de.simpleprojectmanager.simpleprojectmanager.exception;

public class UnknowUserCreateException extends UserCreateException{

    public UnknowUserCreateException() {
        super("user.create.failed");
    }
}
