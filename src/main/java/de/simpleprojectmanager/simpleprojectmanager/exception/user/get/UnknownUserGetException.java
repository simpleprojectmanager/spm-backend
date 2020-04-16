package de.simpleprojectmanager.simpleprojectmanager.exception.user.get;

public class UnknownUserGetException extends UserGetException {
    public UnknownUserGetException() {
        super("user.get.unknown");
    }
}
