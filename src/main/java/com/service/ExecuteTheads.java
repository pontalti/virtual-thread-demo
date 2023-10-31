package com.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.task.MyTask;

@Service
public class ExecuteTheads implements ExecuteThreads {

	private static Logger LOG = LoggerFactory.getLogger(ExecuteTheads.class);

	private final int startThread;
	private final int numOfThreads;
	private int received;
	private ExecutorService executor;
	private ConfigurableApplicationContext context;
	private final CompletionService<Double> completionService;

	public ExecuteTheads(ConfigurableApplicationContext context, ExecutorService executor) {
		super();
		this.startThread = 0;
		this.received = 0;
		this.numOfThreads = 100000;
		this.context = context;
		this.executor = executor;
		this.completionService = new ExecutorCompletionService<Double>(this.executor);
	}

	@Override
	public void prepareData() {
		LocalTime start = LocalTime.now();
		LOG.info("EXECUTING : command line runner");
		LOG.info("Started at -> {}\n", start);
		List<MyTask> list = buildCollection();
		executeThread(list);
		LocalTime end = LocalTime.now();
		LOG.info("Ended at -> {}, time of execution at -> {} @Millis ", end, Duration.between(start, end).toMillis());
		shutdown();
	}

	private List<MyTask> buildCollection() {
		List<MyTask> list = new ArrayList<>();
		IntStream.range(startThread, this.numOfThreads)
					.forEach(i -> list.add(this.context.getBean(MyTask.class)));
		return list;
	}

	private void executeThread(List<MyTask> list) {
		try {
			list.parallelStream()
				.forEach(c->this.completionService.submit(c));
			
			while(this.received < this.numOfThreads) {
				Future<Double> future = this.completionService.take();
				LOG.info("future value {}", future.get());
				this.received ++;
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void shutdown() {
		boolean waiting = true;
		this.executor.shutdown();
		while (!this.executor.isTerminated()) {
			if (waiting) {
				LOG.info("Waiting for thread executor finisha all thread executions.");
				waiting = false;
			}
		}
		this.executor.shutdownNow();
		LOG.info("shutdown thread executor.");
	}

}
