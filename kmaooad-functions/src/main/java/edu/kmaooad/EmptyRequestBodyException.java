package edu.kmaooad;

public class EmptyRequestBodyException extends Exception {

    public EmptyRequestBodyException() {
        super("Request body must not be empty");
    }
    
    public string GetExceptionLog(){
        return "";
    }
    
    public int GetExeptionCode(){
        return 0;
       }

}
