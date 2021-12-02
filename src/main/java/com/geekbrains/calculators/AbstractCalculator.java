package com.geekbrains.calculators;

import java.util.ArrayList;

public abstract class AbstractCalculator {

    protected final String[] OPERATORS = {"%", "/", "*", "-", "+", "^"};

    public AbstractCalculator(){

    }

    public abstract double process(String formula) throws IncorrectFormulaException;

    //Метод дробит формулу на токены
    protected ArrayList<String> tokenize(String formula) throws IncorrectFormulaException{
        ArrayList<String> tokens = new ArrayList<>();
        //System.out.println("Formula = " + formula);

        String regex = "((?=\\*|/|\\+|-|\\^|%)|(?<=\\*|/|\\+|-|\\^|%))";
        String[] tokensBuffer = formula.split(regex);

        String sign = "";
        boolean isPreviousTokenOperator = true;
        for (String token : tokensBuffer) {
            token = token.replace(",", ".");
            boolean isOperator = isOperator(token);
            boolean isNumber = isNumber(token);

            //Специальная обработка для минуса, так как минус может быть знаком перед операндом
            if (isOperator && (isPreviousTokenOperator)) {
                if (token.equals("-")) {
                    if (sign.equals("-")) {
                        //меняем знак обратно
                        sign = "";
                    } else {
                        sign = "-";
                    }
                } else {
                    //Некорректное поведение - два оператора подряд
                    throw new IncorrectFormulaException("Two consequent operators");
                }
            }

            //добавляем оператор
            if (isOperator && !isPreviousTokenOperator) {
                tokens.add(token);
            }

            //для операнда обрабатываем знак
            if (isNumber) {
                token = sign + token;
                sign = "";
                tokens.add(token);
            }

            if (!isOperator&&!isNumber) {
                //Не номер и не оператор
                throw new IncorrectFormulaException("Can not recognize token + " + token);
            }

            isPreviousTokenOperator = isOperator;

        }
        return tokens;
    }

    protected int operatorPriority(String token) {
        switch (token) {
            case "+", "-":
                return 1;
            case "*", "/", "^":
                return 2;
        }
        return 1;
    }

    protected boolean isOperator(String token) {
        for (String operator : OPERATORS)
            if (operator.equals(token))
                return true;
        return false;
    }

    protected boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
