package com.cardmein.drawroyale.game.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a change state attempt on a shoe would place it in an invalid state
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidShoeStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidShoeStateException(String message) {
        super(message);
    }
}
