package edu.kmaooad;

public class EmptyRequestBodyException extends Exception {

    public EmptyRequestBodyException() {
        super("Request body must not be empty");
    }
    
    // Romeved code due to codecov
    // TODO: Extend
}
