package de.simpleprojectmanager.simpleprojectmanager.exception.group.get;

public class UnknownGroupGetException extends GroupGetException {
    public UnknownGroupGetException() {
        super("group.get.unknown");
    }
}
