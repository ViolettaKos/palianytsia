package com.example.palianytsia.exception;

public class WrongPasswordException extends ServiceException{
    public WrongPasswordException() {
        super(ExcConstants.WRONG_PASS);
    }
}
