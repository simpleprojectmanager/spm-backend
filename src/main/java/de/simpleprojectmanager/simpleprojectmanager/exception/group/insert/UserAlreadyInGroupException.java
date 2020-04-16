package de.simpleprojectmanager.simpleprojectmanager.exception.group.insert;

public class UserAlreadyInGroupException extends GroupInsertException {
    public UserAlreadyInGroupException() {
        super("group.insert.exists");
    }
}
