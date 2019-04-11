/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.aop;

import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;

/**
 *
 * @author Totoland
 */
public class AopBefore2 implements MethodBeforeAdvice {
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("BeforeMethod2 : Before method before!");
	}
}
