package de.simpleprojectmanager.simpleprojectmanager.exception;

public class InvalidNickNameException extends UserCreateException{

    public InvalidNickNameException() {
        super("user.create.invalid.nickname");
    }
}
