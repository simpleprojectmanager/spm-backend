package de.simpleprojectmanager.simpleprojectmanager.exception.group.create;

public class UnknownGroupCreateException extends GroupCreateException {
    public UnknownGroupCreateException() {
        super("group.create.failed");
    }
}
