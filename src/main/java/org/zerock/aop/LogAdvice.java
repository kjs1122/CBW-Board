package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect
@Log4j
@Component
public final class LogAdvice {

	@Before("execution(* org.zerock.service.SampleServiceImpl*.doAdd(String,String)) "
			+ "&& args(a,b)")
	public void logBefore(String a, String b) {
		log.info("str1 : " + a);
		log.info("str2 : " + b);
	}
	
	@AfterThrowing(pointcut = "execution(* org.zerock.service.*.*(..))", throwing = "exception")
	public void logException(Exception exception) {
		
		log.info("Exception!................");
		log.info("Exception : " + exception);
	}
	
	@Around("execution(* org.zerock.service.*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		
		long start = System.currentTimeMillis();
		
		log.info("Target : " + pjp.getTarget());
		log.info("Param : " + Arrays.toString(pjp.getArgs()));
		
		//invoke method
		Object result = null;
		
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("Time : " + (end - start));
		return result;
	}
}
