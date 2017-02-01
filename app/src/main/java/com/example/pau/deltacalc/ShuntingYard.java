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

    private enum Operator{
        ADD(0), SUB(0), SUU(3), MUL(1), DIV(1), EXP(2), PAR(-1);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static Map<Character, Operator> ops = new HashMap<Character, Operator>(){{
        put('+', Operator.ADD);
        put('-', Operator.SUB);
        put('_', Operator.SUU);
        put('×', Operator.MUL);
        put('÷', Operator.DIV);
        put('^', Operator.EXP);
        put('(', Operator.PAR);
    }};

    private static boolean isHigherPrec(Operator op1, Operator op2){
        return op1.precedence >= op2.precedence;
    }

    public static String eval(String infix){
        StringBuilder expr = new StringBuilder(infix);
        opStack.clear();
        outputStack.clear();

        if(infix.startsWith("-")) expr.setCharAt(0, '_');
        else if (infix.startsWith("+")) expr.deleteCharAt(0);
        Log.v("INFIX", infix);
        Log.v("EXPR", expr.toString());

        char lastTok = '+';
        for(int i = 0; i < expr.length(); ++i){
            char tok = expr.charAt(i);

            if(ops.containsKey(tok)){
                Log.v("OPS_tok", Character.toString(tok));
                if(ops.containsKey(lastTok) || lastTok == ')'){
                    if (tok == '+') continue;
                    else if (tok == '-') tok = '_';
                    else if (lastTok == ')'){
                        if(tok == '(') {
                            tok = '×';
                            --i;
                        }
                    }
                    Log.v("OPS_tok2", Character.toString(tok));
                }

                evalNumber();

                Operator op = ops.get(tok);
                if(op == Operator.PAR) opStack.push(op);
                else{
                    while (!opStack.isEmpty() && isHigherPrec(opStack.peek(), op)) {
                        popOpToOutput();
                    }
                    opStack.push(op);
                }
            }
            else if(tok == ')'){
                Log.v("RPAR", "right");
                evalNumber();
                while(!opStack.isEmpty() && opStack.peek() != Operator.PAR){
                    popOpToOutput();
                }
                if(opStack.isEmpty()) throw new RuntimeException("Mismatched parentheses");
                else opStack.pop();
            }
            else{
                Log.v("NUM", Character.toString(tok));
                tempNum.append(tok);
            }

            lastTok = tok;
        }
        evalNumber();
        while(!opStack.isEmpty() && opStack.peek() != Operator.PAR){
            popOpToOutput();
        }
        if(outputStack.size() == 0) return "";
        Log.v("OUTPUT", outputStack.peek().toString());
        return outputStack.peek().setScale(15, RoundingMode.HALF_EVEN).toString();
    }

    private static void evalNumber(){
        if(tempNum.length() != 0) {
            if(tempNum.charAt(0) == '.'){
                tempNum = new StringBuilder("0").append(tempNum.toString());
            }
            outputStack.push(new BigDecimal(tempNum.toString()).setScale(15, RoundingMode.HALF_EVEN));
            tempNum.setLength(0);
        }
    }

    private static void popOpToOutput(){
        BigDecimal operandRight = outputStack.peek();
        outputStack.pop();
        BigDecimal operandLeft;

        switch(opStack.peek()){
            case ADD:
                operandLeft = outputStack.peek();
                outputStack.pop();
                outputStack.push(operandLeft.add(operandRight));
                break;
            case SUB:
                operandLeft = outputStack.peek();
                outputStack.pop();
                outputStack.push(operandLeft.subtract(operandRight));
                break;
            case SUU:
                outputStack.push(operandRight.negate());
                break;
            case MUL:
                operandLeft = outputStack.peek();
                outputStack.pop();
                outputStack.push(operandLeft.multiply(operandRight));
                break;
            case DIV:
                operandLeft = outputStack.peek();
                outputStack.pop();
                try {
                    outputStack.push(operandLeft.divide(operandRight, BigDecimal.ROUND_HALF_EVEN));
                }
                catch(ArithmeticException e){
                    throw new RuntimeException("Division by zero");
                }
                break;
            case EXP:
                operandLeft = outputStack.peek();
                outputStack.pop();
                try{
                    outputStack.push(operandLeft.pow(operandRight.intValue()));
                }
                catch(ArithmeticException e){
                    throw new RuntimeException("Bad exponent");
                }
                break;
        }
        opStack.pop();
    }
}
