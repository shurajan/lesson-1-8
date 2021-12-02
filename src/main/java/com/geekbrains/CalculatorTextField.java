package com.geekbrains;

import com.geekbrains.calculators.AbstractCalculator;
import com.geekbrains.calculators.IncorrectFormulaException;

import javax.swing.*;
import java.awt.*;

public class CalculatorTextField extends JTextField {
    private AbstractCalculator calculator;

    CalculatorTextField(AbstractCalculator calculator) {
        super();
        this.calculator = calculator;
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
                } else {
                    try {
                        text = String.format("%32.6f", (-1.0) * calculator.process(text));
                    } catch (IncorrectFormulaException e) {
                        text = "Err";
                    }
                }
                break;
            case "=":
                try {
                    text = String.format("%32.6f", calculator.process(text));
                } catch (IncorrectFormulaException e) {
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

}

