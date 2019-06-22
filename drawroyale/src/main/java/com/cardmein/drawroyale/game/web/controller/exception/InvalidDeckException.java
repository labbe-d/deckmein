package com.cardmein.drawroyale.game.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an operation made on a deck is invalid
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDeckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidDeckException(String message) {
        super(message);
    }
}