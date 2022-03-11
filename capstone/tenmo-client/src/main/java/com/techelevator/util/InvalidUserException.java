package com.techelevator.util;

import javax.security.sasl.AuthenticationException;

public class InvalidUserException extends AuthenticationException {

    private static final long serialVersionUID = -1126699074574529146L;

    public InvalidUserException(String message) {
        super(message);
    }
}