package com.clara.discog.exception;

public class ArtistDuplicatedException extends RuntimeException {
    public ArtistDuplicatedException(String message) {
        super(message);
    }
}
