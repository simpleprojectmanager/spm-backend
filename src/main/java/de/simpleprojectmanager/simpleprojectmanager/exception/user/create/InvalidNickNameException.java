package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class InvalidNickNameException extends UserCreateException{

    public InvalidNickNameException() {
        super("user.create.invalid.nickname");
    }
}
