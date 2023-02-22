package com.example.palianytsia.exception;

public class DuplicatedEmailException extends ServiceException{
    public DuplicatedEmailException() {
        super(ExcConstants.DUPLICATE_EMAIL);
    }
}
