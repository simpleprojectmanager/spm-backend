package de.simpleprojectmanager.simpleprojectmanager.exception.group.create;

public class NameAlreadyUsedException extends GroupCreateException {
    public NameAlreadyUsedException() {
        super("group.create.name.used");
    }
}
