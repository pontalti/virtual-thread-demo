package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.service.ExecuteThreads;

@SpringBootApplication
public class ThreadDemoMain implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(ThreadDemoMain.class);

	private ConfigurableApplicationContext context;
	private ExecuteThreads executeThread;

	public ThreadDemoMain(ConfigurableApplicationContext context, ExecuteThreads executeThread) {
		super();
		this.executeThread = executeThread;
		this.context = context;
	}

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(ThreadDemoMain.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {
		this.executeThread.prepareData();
		this.context.close();
	}

}
