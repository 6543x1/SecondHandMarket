package com.jessie.SHMarket.exception;

public class WrongFileTypeException extends RuntimeException
{
    public WrongFileTypeException()
    {

    }

    public WrongFileTypeException(String message)
    {
        super(message);
    }
}
