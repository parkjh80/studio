package com.samsung.sds.brightics.lightweight.job.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samsung.sds.brightics.lightweight.job.JobRunnerContext;
import com.samsung.sds.brightics.lightweight.job.JobRunnerWrapper;

public class jobRunnerThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(jobRunnerThread.class);
	private final Socket socket;
	private final ThreadPoolExecutor threadPoolExecutor;
	private final JobRunnerContext jobRunnerContext;

	public jobRunnerThread(Socket socket, ThreadPoolExecutor threadPoolExecutor) {
		String remoteAddress = socket.getRemoteSocketAddress().toString();
		if (remoteAddress != null) {
			setName(remoteAddress);
		}
		this.socket = socket;
		this.threadPoolExecutor = threadPoolExecutor;
		this.jobRunnerContext = new JobRunnerContext();
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String input = in.readLine();
			if (input != null) {
				StringTokenizer st = new StringTokenizer(input);
				String modelPath = st.nextToken();
				try {
					threadPoolExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								JobRunnerWrapper jobRunable = jobRunnerContext.createJobRunner(modelPath);
								jobRunable.setTimeout(threadPoolExecutor.getCorePoolSize());
								jobRunable.setUser(Thread.currentThread().getName());
								String threadName = Thread.currentThread().getName();
								long startTime = System.currentTimeMillis();
								logger.info(String.format(
										"(%s)[LIGHTWEIGHT MODEL START] path : %s, active count: %s, run count :%s",
										threadName, modelPath, threadPoolExecutor.getActiveCount() + "",
										threadPoolExecutor.getTaskCount() + ""));
								jobRunable.run();
								logger.info(String.format(
										"(%s)[LIGHTWEIGHT MODEL FINISH] path : %s, status : %s, elapsed time : %s",
										threadName, modelPath, jobRunable.getStatus(),
										(System.currentTimeMillis() - startTime) + ""));
							} catch (Exception e) {
								logger.error("Cannot run model.", e);

							}
						}
					});
				} catch (Exception e) {
					logger.error("Cannot run work flow model", e);
				}

			}
		} catch (Exception e) {
			logger.error("Cannt read inputstream.", e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Cannot close socket.", e);
			}
		}
	}

}