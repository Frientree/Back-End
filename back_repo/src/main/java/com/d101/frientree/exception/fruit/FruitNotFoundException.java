package com.d101.frientree.exception.fruit;

public class FruitNotFoundException extends RuntimeException{
    public FruitNotFoundException(String message){
        super(message);
    }
}
