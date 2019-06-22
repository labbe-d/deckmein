package com.cardmein.drawroyale.game.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an operation on a player is invalid
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPlayerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidPlayerException(String message) {
        super(message);
    }
}