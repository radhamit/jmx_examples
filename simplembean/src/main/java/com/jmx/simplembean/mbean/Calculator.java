package com.jmx.simplembean.mbean;

public class Calculator implements CalculatorMBean {

	@Override
	public int add(int a, int b) {
		return (a + b);
	}

	@Override
	public int subtract(int a, int b) {
		return (a - b);
	}

	@Override
	public int multiply(int a, int b) {		
		return (a * b);
	}

	@Override
	public int divide(int a, int b) {
		int result = 0;
		if(b == 0) {
			result = -1;
		} else {
			result = (a / b);
		}
		return result;
	}

}
