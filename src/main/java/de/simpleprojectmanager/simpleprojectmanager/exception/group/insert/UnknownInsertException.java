package de.simpleprojectmanager.simpleprojectmanager.exception.group.insert;

public class UnknownInsertException extends GroupInsertException{
    public UnknownInsertException() {
        super("group.insert.unknown");
    }
}