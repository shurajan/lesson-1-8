package com.geekbrains.calculators;

public class IncorrectFormulaException extends Exception{

    public IncorrectFormulaException() {}

    public IncorrectFormulaException(String message)
    {
        super(message);
    }
}
