package com.example.mycalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import android.util.Log;

public class  ExpressionProcess {
	String exp;
	Vector<String> postfixExp = new Vector<String>();
	Map<String , Integer > precedenceTable = new HashMap<String, Integer>();
	ExpressionProcess(){
		precedenceSetup();
	}
	
	void precedenceSetup(){
		precedenceTable.put("x", 12);
		precedenceTable.put("/", 12);
		precedenceTable.put("%", 12);
		precedenceTable.put("+", 11);
		precedenceTable.put("-", 11);
//		precedenceTable.put("(", 15);
//		precedenceTable.put("*", 12);
//		precedenceTable.put("*", 12);
//		precedenceTable.put("*", 12);
		
	}
	
	public int precedenceOf(char c){
		return precedenceTable.get(c);
	}
	
	boolean process(String str, float result){
		exp = new String(str);
		return convertToPostfixExp(result);
		//return true;
	}
	
	boolean isNegativeOp(int i){
		if(exp.charAt(i) == '-'){
			if(i == 0)
				return true;
			
			if( isOperator(exp.charAt(i-1)) )
				return true;
		}
		return false;
	}
	
	boolean convertToPostfixExp(Float result){
		Stack<Character> op = new Stack<Character>();
		int index =0;
		while(index < exp.length()){
			//if operator push to stack
			if(!isOperator(exp.charAt(index)) || isNegativeOp(index) ){
				StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (index < exp.length() && exp.charAt(index) >= '0' && exp.charAt(index) <= '9'){
                    sbuf.append(exp.charAt(index));
                    index++;
                    
                }
                postfixExp.add(sbuf.toString());
                continue;
//				postfixExp += exp.charAt(index);
//				index++;
//				continue;
			}
			
			// if left paranthesis 
			if(exp.charAt(index) == '('){
				op.push(exp.charAt(index));
//				index++;
			}
			
			// if right paranthesis is found
			if(exp.charAt(index) == ')'){
				while( !op.empty() && op.peek() != '('){
					postfixExp.add(op.pop().toString());
				}
				if(!op.empty())
					op.pop();
//				index++;
			}
			
			// if an operator is found
			if(isOperator(exp.charAt(index)) ){
				if(op.empty() || op.peek() == '('){
					op.push(exp.charAt(index));
//					index++;
				}
				else{
					while(!op.empty() && op.peek()!= '(' &&
							precedenceOf(exp.charAt(index)) < precedenceOf(op.peek() ) ){
						postfixExp.add(op.pop().toString());
					}
					op.push(exp.charAt(index));
//					index++;
				}
			
			}
			index++;
		}
		while(!op.empty()){
			postfixExp.add(op.pop().toString());
		}

		for(int i = 0; i< postfixExp.size(); ++i){
			Log.d("vaman", " exp == "+ i+ " content "+postfixExp.get(i));
		}
		// evaluate postfix expresstion
		Stack<Float> temp = new Stack<Float>();
		for(int i = 0; i< postfixExp.size(); ++i){
			if(isOperator(postfixExp.get(i) ) ){
				if(temp.size() < 2)
					return false;
				Float op2 = temp.pop();
				Float op1 = temp.pop();
				Log.d("vaman", " value retuned " + performOperation(op1, op2, postfixExp.get(i)));
				temp.push(performOperation(op1, op2, postfixExp.get(i)));
			}
			else
				temp.push(stringToNumber(postfixExp.get(i)));
		}
		
		if(temp.size() != 1)
			return false;
		else{
			result = temp.pop();
		}
		
		return true;
	}
	
	public Float stringToNumber(String str){
		return Float.parseFloat(str);
	}
	
	public Float performOperation(Float op1, Float op2, String str){
		switch(str.charAt(0)){
		case '+': return (Float)(op1+op2);
		case '-': return (Float)(op1+op2);
		case 'x': return (Float)(op1*op2);
		case '/': return (Float)(op1/op2);

		}
		return (float) 0;
	}
	
	private boolean isOperator(String charAt) {
		// TODO Auto-generated method stub
		if(charAt.equals("x") ||charAt.equals("+") || charAt.equals("-") || charAt.equals("/") )
			return true;
		return false;
	}

	private boolean isOperator(char charAt) {
		// TODO Auto-generated method stub
		if(charAt == 'x' ||charAt == '+' ||charAt == '-' ||charAt == '/')
			return true;
		return false;
	}
}