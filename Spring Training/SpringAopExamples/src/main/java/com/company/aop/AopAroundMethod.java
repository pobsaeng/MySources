package com.company.aop;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AopAroundMethod implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		System.out.println("Method name : "
				+ methodInvocation.getMethod().getName());
		System.out.println("Method arguments : "
				+ Arrays.toString(methodInvocation.getArguments()));

		// same with MethodBeforeAdvice
		System.out.println("AroundMethod : Before method invoke!");

		try {
			// proceed to original method call
			Object result = methodInvocation.proceed();

			// same with AfterReturningAdvice
			System.out.println("AroundMethod : Before after invoke!");

			return result;

		} catch (IllegalArgumentException e) {
			// same with ThrowsAdvice
			System.out
					.println("AroundMethod : Throw exception invoke!");
			throw e;
		}
	}
}