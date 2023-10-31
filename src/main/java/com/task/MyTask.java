package com.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Scope("prototype")
public class MyTask implements Callable<Double> {
	
	private static Logger LOG = LoggerFactory.getLogger(MyTask.class);
	
	public MyTask() {
		super();
	}

	@Override
	public Double call() throws Exception {
		Double d = Math.random();
		LOG.info("Executing {}", Thread.currentThread());
		return d;
	}

}