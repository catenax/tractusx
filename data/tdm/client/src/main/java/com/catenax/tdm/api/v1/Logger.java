package com.catenax.tdm.api.v1;

import org.threeten.bp.LocalDateTime;

public class Logger {
	
	private Class clazz;
	
	public Logger(Class clazz) {
		this.clazz = clazz;
	}
	
	public void info(String message) {
		System.out.println("[INFO " + LocalDateTime.now().toString() + "] " + clazz.getCanonicalName() + ":\t" + message);
	}
	
	public void error(String message) {
		System.err.println("[ERROR " + LocalDateTime.now().toString() + "] " + clazz.getCanonicalName() + ":\t" + message);
	}
	
	public void error(Throwable error) {
		error.printStackTrace();
	}

}
