package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class UnknowUserCreateException extends UserCreateException{

    public UnknowUserCreateException() {
        super("user.create.failed");
    }
}
