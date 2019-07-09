package com.revature.aop;

import java.time.LocalTime;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * This is a tentative outline for future batches to use to help log what is happening across methods. 
 * Uses Spring AOP to log the inputs and outputs of ALL methods in ALL packages.
 * This class is mirrored in the project-service and should be updated similarly to this one. 
 * @TODO An attempt was made to print out the class the method is in, but there was not enough time to implement correctly.
 * @TODO The project owner has requested replacing sysouts with SLF4J logs to be persisted to another file.
 * 
 * @author Daniel Shaffer (190422-USF-Java)
 */
@Aspect
@Component
public class AuthAspects {
		
	/**
	 * This AOP annotation is used to parameterize pointcut expressions.
	 * Currently, this pointcut joins to ALL methods in ALL packages.
	 * This can be too much information and should be edited as needed.
	 * 
	 * @author Daniel Shaffer (190422-USF-Java)
	 */
    @Pointcut("execution(* com.revature..*(..))")
    public void logAll() {
    }
	
    /**
     * beforeExec() logs basic information before a function is even executed.
     * This includes the local time the method was called, 
     * the name of the method, and the input arguments.
     * 
	 * @author Daniel Shaffer (190422-USF-Java)
     */
	@Before("logAll()")
	public void beforeExec(JoinPoint jp) {
		System.out.println("[LOG] - [auth-service]");
		System.out.println("	Timestamp: "+LocalTime.now());
		System.out.println("	Before method: "+jp.getSignature().getName());
		System.out.println("	Input arguments: "+Arrays.toString(jp.getArgs()));
	}
	
    /**
     * afterReturn() logs basic information after a function returns successfully.
     * This includes the local time the method was finished,
     * the name of the method, and the returned outputs.
     * 
	 * @author Daniel Shaffer (190422-USF-Java)
     */
	@AfterReturning(pointcut="logAll()",returning="result")
	public void afterReturn(JoinPoint jp, Object result) {
		System.out.println("[LOG] - [auth-service]");
		System.out.println("	Timestamp: "+LocalTime.now());
		System.out.println("	After method: "+jp.getSignature().getName());
		System.out.println("	Returned values: "+result+"\n");
	}

    /**
     * errorOccurance() is built on legacy code and logs when and where an error is caught.
     * 
	 * @author Daniel Shaffer (190422-USF-Java)
     */
	@AfterThrowing(pointcut = "logAll()", throwing = "ex")
	public void errorOccurance(JoinPoint jp, Exception ex){
		System.out.println("[ERROR] - [auth-service]");
		System.out.println("	Errored method: "+jp.getSignature().getName());
		System.out.println("	Error caught: "+ex.getMessage());
	}
	
}