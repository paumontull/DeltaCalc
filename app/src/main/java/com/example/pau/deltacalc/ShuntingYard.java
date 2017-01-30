package com.example.pau.deltacalc;

import android.util.Log;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Pau Montull i Jové on 29/1/17.
 */

public class ShuntingYard {

    private static Stack<Operator> opStack = new Stack<>();
    private static Stack<BigDecimal> outputStack = new Stack<>();
    private static StringBuilder tempNum = new StringBuilder();

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
        return op1.precedence > op2.precedence || (op1.precedence == op2.precedence && op1 == Operator.EXP);
    }

    public static String postfix(String infix){
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
                processDigit(tok);
            }
        }
        processNumber();
        while(!opStack.isEmpty() && outputStack.size() > 1){
            popOpToOutput();
        }
        if(outputStack.isEmpty()) return "";
        else return outputStack.peek().setScale(12, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    private static void processNumber() {
        if(tempNum.length() != 0){
            outputStack.push(new BigDecimal(tempNum.toString()));
            tempNum.setLength(0);
        }
    }

    public static void processDigit(char tok){
        tempNum.append(tok);
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
                        /* Mismatched parenthesis */
        }
        else{
            opStack.pop();
        }
    }

    private static void popOpToOutput(){
        BigDecimal operand1 = outputStack.peek();
        outputStack.pop();
        BigDecimal operand2 = outputStack.peek();
        outputStack.pop();

        switch(opStack.peek()){
            case ADD:
                outputStack.push(operand1.add(operand2));
                break;
            case SUB:
                outputStack.push(operand2.subtract(operand1));
                break;
            case MUL:
                outputStack.push(operand1.multiply(operand2));
                break;
            case DIV:
                outputStack.push(operand1.divide(operand2, BigDecimal.ROUND_HALF_EVEN));
                break;
            case EXP:
                outputStack.push(operand1.pow(operand2.intValue()));
                break;
        }
        opStack.pop();
    }
}
