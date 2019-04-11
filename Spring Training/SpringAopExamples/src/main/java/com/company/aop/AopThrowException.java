package com.company.aop;

import org.springframework.aop.ThrowsAdvice;

public class AopThrowException implements ThrowsAdvice {
	public void afterThrowing(IllegalArgumentException e) throws Throwable {
		System.out.println("ThrowException : Throw exception afterThrowing! : "+e.toString());
	}
}