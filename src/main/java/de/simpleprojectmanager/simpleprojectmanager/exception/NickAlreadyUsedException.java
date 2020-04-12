package de.simpleprojectmanager.simpleprojectmanager.exception;

public class NickAlreadyUsedException extends UserCreateException{
    public NickAlreadyUsedException() {
        super("user.create.used.nickname");
    }
}
