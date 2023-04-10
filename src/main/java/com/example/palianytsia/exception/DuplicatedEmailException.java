package com.example.palianytsia.exception;

public class DuplicatedEmailException extends ServiceException {
    private static final String DUPLICATE_EMAIL = "duplicated_email";

    public DuplicatedEmailException() {
        super(DUPLICATE_EMAIL);
    }
}
