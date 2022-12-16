package edu.kmaooad;

public class EmptyRequestBodyException extends Exception {

    public EmptyRequestBodyException() {
        super("Request body must not be empty");
    }
    
    public String GetExceptionLog(){
        return "";
    }
    
    public int GetExeptionCode(){
        return 0;
       }

}
