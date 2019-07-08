//package com.revature.aop;
//
//import java.time.Duration;
//import java.time.LocalTime;
//import java.util.Arrays;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class AuthAspects {
//	
//	LocalTime now;
//	
//    @Pointcut("execution(* com.revature..*(..))")
//    public void logAll() {
//    }
//	
//	@Before("logAll()")
//	public void beforeExec(JoinPoint jp) {
//		this.now = LocalTime.now();
//		System.out.println("[LOG] - [auth-service]");
//		System.out.println("	Before method: 		"+jp.getClass()+" "+jp.getSignature().getName());
//		System.out.println("	Timestamp: 		"+this.now);
//		System.out.println("	Input arguments: 	"+Arrays.toString(jp.getArgs()));
//	}
//	
//	@AfterReturning(pointcut="logAll()",returning="result")
//	public void afterReturn(JoinPoint jp, Object result) {
//		System.out.println("[LOG] - [auth-service]");
//		System.out.println("	After method: 			"+jp.getClass()+" "+jp.getSignature().getName());
//		System.out.println("	Milliseconds to complete: 	"+Duration.between(this.now,LocalTime.now()));
//		System.out.println("	Returned values: 		"+result+"\n");
//	}
//
//	@AfterThrowing(pointcut = "logAll()", throwing = "ex")
//	public void errorOcurance(JoinPoint joinPoint, Exception ex){
//		System.out.println("[ERROR]-[auth-service] - Error caught: "+ex.getMessage());
//	}
//	
//
//}
