package com.geekbrains;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class CalculatorTextField extends JTextField {

    private final String[] OPERATORS = {"%", "/", "*", "-", "+", "^"};
    private Stack<String> stackOutput = new Stack<String>();
    private Stack<String> stackOperators = new Stack<String>();
    private Stack<String> stackCalculator = new Stack<String>();

    CalculatorTextField() {
        super();
        setText("");
        setHorizontalAlignment(SwingConstants.RIGHT);
        setFont(new Font("Arial", Font.PLAIN, 40));
        setMaximumSize(new Dimension(400, 100));
        setEnabled(false);
    }

    public void process(String action) {
        String text = getText().trim();

        if (text.matches(".*[a-zA-Z].*")) {
            text = "";
            setText("");
        }

        switch (action) {
            case "AC":
                text = "";
                break;
            case "+/-":
                if (text.equals("")) {
                    text = "-";
                } else if (text.equals("-")) {
                    text = "";
                } else if (evaluate(text)) {
                    try {
                        text = String.format("%32.6f", (-1.0) * calculate());
                    } catch (EmptyStackException e) {
                        text = "Err";
                    }
                } else {
                    text = "Err";
                }
                break;
            case "=":
                if (text.equals("")) {
                    break;
                } else if (evaluate(text)) {
                    try {
                        text = String.format("%32.6f", calculate());
                    } catch (EmptyStackException e) {
                        text = "Err";
                    }
                } else {
                    text = "Err";
                }
                break;

            default:
                text = text + action;
                break;
        }

        text = text.replace(".", ",");
        text = (text.indexOf(",") >= 0 ? text.replaceAll("\\,?0+$", "") : text);
        setText(text);

    }

    private boolean evaluate(String formula) {
        ArrayList<String> tokens = new ArrayList<>();
        //System.out.println("Formula = " + formula);

        String regex = "((?=\\*|/|\\+|-|\\^|%)|(?<=\\*|/|\\+|-|\\^|%))";
        String[] tokensBuffer = formula.split(regex);

        String sign = "";
        boolean isPreviousTokenOperator = true;
        for (String token : tokensBuffer) {
            token = token.replace(",", ".");
            boolean isOperator = isOperator(token);

            if (isOperator && (isPreviousTokenOperator)) {
                if (token.equals("-")) {
                    if (sign.equals("-")) {
                        //меняем знак обратно
                        sign = "";
                    } else {
                        sign = "-";
                    }
                } else {
                    //Некорректное поведение
                    return false;
                }
            }

            if (isOperator && !isPreviousTokenOperator) {
                tokens.add(token);
            }

            if (isNumber(token)) {
                token = sign + token;
                sign = "";
                tokens.add(token);
            } else if (!isOperator) {
                //Не номер и не оператор
                return false;
            }

            isPreviousTokenOperator = isOperator;

        }

        for (String token : tokens) {
            if (isOperator(token)) {
                while (!stackOperators.empty()
                        && (operatorPriority(token)
                        <= operatorPriority(stackOperators.lastElement()))) {
                    stackOutput.push(stackOperators.pop());
                }
                stackOperators.push(token);

            } else {
                stackOutput.push(token);
            }
        }

        while (!stackOperators.empty()) {
            stackOutput.push(stackOperators.pop());
        }

        Collections.reverse(stackOutput);
        return true;
    }

    public double calculate() throws EmptyStackException {

        while (!stackOutput.empty()) {
            String token = stackOutput.pop();
            //System.out.println(token);
            if (isOperator(token)) {
                try {
                    double operand1 = Double.parseDouble(stackCalculator.pop());
                    double operand2 = Double.parseDouble(stackCalculator.pop());
                    switch (token) {
                        case "+":
                            stackCalculator.push(String.valueOf(operand2 + operand1));
                            break;
                        case "-":
                            stackCalculator.push(String.valueOf(operand2 - operand1));
                            break;
                        case "%":
                            stackCalculator.push(String.valueOf(operand2 % operand1));
                            break;
                        case "*":
                            stackCalculator.push(String.valueOf(operand2 * operand1));
                            break;
                        case "/":
                            stackCalculator.push(String.valueOf(operand2 / operand1));
                            break;
                        case "^":
                            stackCalculator.push(String.valueOf(Math.pow(operand2, operand1)));
                            break;
                    }
                } catch (EmptyStackException e) {
                    throw e;
                }


            } else {
                stackCalculator.push(token);
            }

        }

        return Double.parseDouble(stackCalculator.pop());
    }

    private int operatorPriority(String token) {
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

