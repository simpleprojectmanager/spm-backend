package de.simpleprojectmanager.simpleprojectmanager.exception.group.create;

public class InvalidNameException extends GroupCreateException{
    public InvalidNameException() {
        super("group.create.invalid.name");
    }
}
