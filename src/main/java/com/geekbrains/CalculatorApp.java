package com.geekbrains;

import com.geekbrains.calculators.ReversePolishNotationCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorApp extends JFrame {


    private static String[] actionButtonsText = {"AC", "+/-", "%", "/", "*", "-", "+", "^", ",", "="};

    //Buttons
    private static JButton[] actionButtons = new JButton[10];
    private static JButton[] digitalButtons = new JButton[10];
    private static JPanel buttonsMiddlePanel = new JPanel();

    public CalculatorApp() {

        ReversePolishNotationCalculator rpnCalculator = new ReversePolishNotationCalculator();
        CalculatorTextField textField = new CalculatorTextField(rpnCalculator);

        setBounds(500, 500, 450, 330);
        setTitle("Calculator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());
        for (int i = 0; i < 10; i++) {

            digitalButtons[i] = new JButton(String.valueOf(i));
            digitalButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    textField.process(actionEvent.getActionCommand());
                }
            });

            actionButtons[i] = new JButton(actionButtonsText[i]);
            actionButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            actionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    textField.process(actionEvent.getActionCommand());
                }
            });

        }

        buttonsMiddlePanel.setLayout(new GridLayout(5, 4));
        for (int i = 0; i < 4; i++) {
            buttonsMiddlePanel.add(actionButtons[i]);
        }

        int j = 4;
        for (int i = 1; i < 10; i++) {
            buttonsMiddlePanel.add(digitalButtons[i]);
            if (i % 3 == 0) {
                buttonsMiddlePanel.add(actionButtons[j]);
                j++;
            }
        }

        buttonsMiddlePanel.add(actionButtons[7]);
        buttonsMiddlePanel.add(digitalButtons[0]);
        buttonsMiddlePanel.add(actionButtons[8]);
        buttonsMiddlePanel.add(actionButtons[9]);


        add(textField, BorderLayout.NORTH);
        add(buttonsMiddlePanel, BorderLayout.CENTER);
        setVisible(true);

    }

    public static void main(String[] args) {
        new CalculatorApp();
    }


}
