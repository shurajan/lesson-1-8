package com.geekbrains.calculators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class ReversePolishNotationCalculator extends AbstractCalculator {


    @Override
    public double process(String formula) throws IncorrectFormulaException {
        //Обрабатываем ситуацию когда формула пуста или содержит символы в результате предыдущей операции.
        //Может содержать Err или Infinite при делении на 0
        if (formula.equals("") || formula.matches(".*[a-zA-Z].*")) {
            return 0;
        }

        Stack<String> stackOutput = createOutputStack(formula);
        Stack<String> stackCalculator = new Stack<>();

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
                    throw new IncorrectFormulaException("Incorrect formula");
                }


            } else {
                stackCalculator.push(token);
            }

        }

        return Double.parseDouble(stackCalculator.pop());
    }

    //создаем выходной стек обратной польской записи
    private Stack<String> createOutputStack(String formula) throws IncorrectFormulaException {
        ArrayList<String> tokens = tokenize(formula);

        Stack<String> stackOperators = new Stack<String>();
        Stack<String> stackOutput = new Stack<String>();

        for (String token : tokens) {
            if (isOperator(token)) {
                while (!stackOperators.empty() &&
                        (operatorPriority(token) <= operatorPriority(stackOperators.lastElement()))) {
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
        return stackOutput;
    }
}
