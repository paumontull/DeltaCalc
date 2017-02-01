package com.example.pau.deltacalc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Pau Montull i Jové on 31/1/17.
 */

public class RecursiveDescentParser {

    private enum errno{
        NO_ERR, PAR_ERR, WREXP_ERR, DIVZER_ERR;
    }

    private static final int scale = 15;
    private static errno e = errno.NO_ERR;
    private static int parCount = 0;

    private static BigDecimal getTerm(StringBuilder expr){
        StringBuilder atomBuilder = new StringBuilder();
        int i = 0;
        while(i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')){
            atomBuilder.append(expr.charAt(i));
            ++i;
        }
        expr.delete(0, i);
        return new BigDecimal(atomBuilder.toString()).setScale(scale, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal parseAtom(StringBuilder expr){
        BigDecimal result = null;
        if(Character.isDigit(expr.charAt(0))){
            return getTerm(expr);
        }
        else if(expr.charAt(0) == '-'){
            return parseAtom(expr.deleteCharAt(0)).negate();
        }
        else if(expr.charAt(0) == '('){
            parCount++;
            result = parseSummands(expr.deleteCharAt(0));
            if(expr.charAt(0) != ')') e = errno.PAR_ERR;
            parCount--;
        }
        return result;
    }

    private static BigDecimal parseFactors(StringBuilder expr){
        BigDecimal leftOperand = parseAtom(expr);
        while(expr.length() != 0 && (expr.charAt(0) == '×' || expr.charAt(0) == '÷')){
            char op = expr.charAt(0);
            BigDecimal rightOperand = parseAtom(expr.deleteCharAt(0));
            if(op == '÷'){
                try{
                    leftOperand = leftOperand.divide(rightOperand, BigDecimal.ROUND_HALF_EVEN);
                }
                catch(java.lang.ArithmeticException exception){
                    e = errno.DIVZER_ERR;
                }
            }
            else leftOperand = leftOperand.multiply(rightOperand);
        }
        return leftOperand;
    }

    private static BigDecimal parseSummands(StringBuilder expr){
        BigDecimal leftOperand = parseFactors(expr);
        while(expr.length() != 0 && (expr.charAt(0) == '+' || expr.charAt(0) == '-')){
            char op = expr.charAt(0);
            BigDecimal rightOperand = parseAtom(expr.deleteCharAt(0));
            if(op == '+'){
                leftOperand = leftOperand.add(rightOperand);
            }
            else leftOperand = leftOperand.subtract(rightOperand);
        }
        return leftOperand;
    }

    public static String eval(String expr){
        if(expr.length() != 0){
            StringBuilder exprBuilder = new StringBuilder(expr);
            BigDecimal result = parseSummands(exprBuilder);
            if(e != errno.NO_ERR) return "";
            else return result.toString();
        }
        else return "";
    }
}
