package de.simpleprojectmanager.simpleprojectmanager.exception.group.create;

public class InvalidPermissionsException extends GroupCreateException {
    public InvalidPermissionsException() {
        super("group.create.invalid.permission");
    }
}
