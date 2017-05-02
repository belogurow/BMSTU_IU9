package com.belogurow;

/**
 * Created by alexbelogurow on 02.05.17.
 */
public class Error {
    private String text;
    private Position position;

    public Error(String text, Position position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Error: " + text + " on " + position;
    }
}
