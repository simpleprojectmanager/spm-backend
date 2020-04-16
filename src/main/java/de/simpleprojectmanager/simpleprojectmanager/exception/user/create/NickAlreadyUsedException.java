package de.simpleprojectmanager.simpleprojectmanager.exception.user.create;

public class NickAlreadyUsedException extends UserCreateException{
    public NickAlreadyUsedException() {
        super("user.create.used.nickname");
    }
}
