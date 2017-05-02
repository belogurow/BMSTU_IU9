package com.belogurow;

/**
 * Created by alexbelogurow on 25.04.17.
 */


public class Token {
    private final DomainTag tag;
    private final Fragment coords;
    private String value;


    protected Token(DomainTag tag, String value, Position starting, Position following){
        this.tag = tag;
        this.value = value;
        this.coords = new Fragment(starting, following);

    }


    @Override
    public String toString() {
        String name = tag.name();
        if (name.charAt(name.length() - 2) == '_') {
            name = name.substring(0, name.length() - 2);
        } else if (name.charAt(name.length() - 3) == '_') {
            name = name.substring(0, name.length() - 3);
        }

        return name + " " + coords.toString() + ": " + value;
    }
}