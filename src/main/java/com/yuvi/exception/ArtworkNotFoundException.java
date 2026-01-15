package com.yuvi.exception;

public class ArtworkNotFoundException extends RuntimeException {
    public ArtworkNotFoundException(String message) {
        super(message);
    }
}
