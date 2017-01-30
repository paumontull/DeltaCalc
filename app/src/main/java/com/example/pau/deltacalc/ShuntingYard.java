package com.example.pau.deltacalc;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class ShuntingYard {

    private static Stack<Operator> opStack = new Stack<>();
    private static Stack<BigDecimal> outputStack = new Stack<>();
    private static StringBuilder tempNum = new StringBuilder();
    private static boolean exception = false;
    private static String msg;

    private enum Operator{
        ADD(0), SUB(0), MUL(1), DIV(1), EXP(2), PAR(-1);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static Map<Character, Operator> ops = new HashMap<Character, Operator>(){{
        put('+', Operator.ADD);
        put('-', Operator.SUB);
        put('×', Operator.MUL);
        put('÷', Operator.DIV);
        put('^', Operator.EXP);
        put('(', Operator.PAR);
    }};

    private static boolean isHigherPrec(Operator op1, Operator op2){
        return op1.precedence >= op2.precedence;
    }

    public static String postfix(String infix){
        exception = false;
        msg = "";
        opStack.clear();
        outputStack.clear();
        for(int i = 0; i < infix.length(); ++i){
            char tok = infix.charAt(i);

            //Check for operator
            if(ops.containsKey(tok)){
                processOperator(tok);
            }
            else if(tok == ')'){
                processRPar();
            }
            else{
                tempNum.append(tok);
            }
        }
        processNumber();
        while(!opStack.isEmpty() && outputStack.size() > 1){
            popOpToOutput();
        }
        if(exception) return msg;
        else if(outputStack.isEmpty()) return "";
        else return outputStack.peek().setScale(12, RoundingMode.HALF_EVEN).toString();
    }

    private static void processNumber() {
        if(tempNum.length() != 0){
            if(tempNum.toString().charAt(0) == '.'){
                tempNum = new StringBuilder("0").append(tempNum.toString());
            }
            else{
                outputStack.push(new BigDecimal(tempNum.toString()).setScale(15, RoundingMode.HALF_EVEN));
                tempNum.setLength(0);
            }
        }
    }

    public static void processOperator(char tok){
        processNumber();

        Operator op = ops.get(tok);
        if(op == Operator.PAR) opStack.push(op);
        else{
            while(!opStack.isEmpty() && isHigherPrec(opStack.peek(), op)){
                popOpToOutput();
            }
            opStack.push(op);
        }
    }

    public static void processRPar(){
        processNumber();

        //pop operators until closing parenthesis
        while(!opStack.isEmpty() && opStack.peek() != Operator.PAR){
            popOpToOutput();
        }
        if(opStack.isEmpty()) {
            exception = true;
            msg = "Mismatched parenthesis";
        }
        else{
            opStack.pop();
        }
    }

    private static void popOpToOutput(){
        BigDecimal operandRight = outputStack.peek();
        outputStack.pop();
        BigDecimal operandLeft = outputStack.peek();
        outputStack.pop();

        switch(opStack.peek()){
            case ADD:
                outputStack.push(operandLeft.add(operandRight));
                break;
            case SUB:
                outputStack.push(operandLeft.subtract(operandRight));
                break;
            case MUL:
                outputStack.push(operandLeft.multiply(operandRight));
                break;
            case DIV:
                try{
                    outputStack.push(operandLeft.divide(operandRight, BigDecimal.ROUND_HALF_EVEN));
                } catch(java.lang.ArithmeticException e){
                    exception = true;
                    msg = e.getMessage();
                }
                break;
            case EXP:
                outputStack.push(operandLeft.pow(operandRight.intValue()));
                break;
        }
        opStack.pop();
    }
}
