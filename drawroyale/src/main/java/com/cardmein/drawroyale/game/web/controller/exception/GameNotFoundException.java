package com.cardmein.drawroyale.game.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a REST call is made on a game that does not exist
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
}