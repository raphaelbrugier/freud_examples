package com.github.freudExamples.devoxx.services;

import java.util.Date;

import org.joda.time.DateTime;

public class TimeProvider {
	
	public static final TimeProvider INSTANCE = new TimeProvider();
	
	public Date getCurrentDate() {
		return  new DateTime().toDate(); 
	}
}
