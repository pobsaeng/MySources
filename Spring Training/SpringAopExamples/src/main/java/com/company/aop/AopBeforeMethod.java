package com.company.aop;

import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;

public class AopBeforeMethod implements MethodBeforeAdvice {
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("BeforeMethod : Before method before!");
	}
}