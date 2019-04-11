package com.company.aop;

import java.lang.reflect.Method;
import org.springframework.aop.AfterReturningAdvice;

public class AopAfterMethod implements AfterReturningAdvice {

	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		System.out.println("AfterMethod : After method hijacked!");
	}
}