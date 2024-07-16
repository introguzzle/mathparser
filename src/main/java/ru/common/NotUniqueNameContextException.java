package ru.common;

import java.util.Set;

public class NotUniqueNameContextException extends ContextException {
    public NotUniqueNameContextException(String name, Set<String> names) {
        super("Name already exists in context: " + name + ". Current names are: " + names);
    }
}
