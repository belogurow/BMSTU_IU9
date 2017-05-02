package com.belogurow;

/**
 * Created by alexbelogurow on 25.04.17.
 */
public class Fragment {
    private Position starting, following;


    public Fragment( Position starting, Position following ){
        this.starting = starting ;
        this.following = following ;
    }
    @Override
    public String toString( ) {
        return starting.toString()  + "−" + following.toString();
    }
}
