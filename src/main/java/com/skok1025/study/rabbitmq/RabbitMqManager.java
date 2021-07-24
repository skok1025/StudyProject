package com.skok1025.study.rabbitmq;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMqManager implements ShutdownListener {

	private final static Logger LOGGER = Logger.getLogger(RabbitMqManager.class.getName());
	private final ConnectionFactory factory;
	private final ScheduledExecutorService executor;
	private volatile Connection connection;
	
	public RabbitMqManager(final ConnectionFactory factory) {
		this.factory = factory;
		executor = Executors.newSingleThreadScheduledExecutor();
		connection = null;
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException cause) {
		// 예기치 않은 문제가 발생할 때, 재연결
		if (!cause.isInitiatedByApplication()) {
			LOGGER.log(Level.SEVERE, "Lost connection to " + factory.getHost() + ":" + factory.getPort(), cause);
			connection = null;
			asyncWaitAndReconnect();
		}
	}
	
	protected void asyncWaitAndReconnect() {
		executor.schedule(new Runnable() {
			
			@Override
			public void run() {
				start();
			}
		}, 15, TimeUnit.SECONDS);
	}
	
	public void start() {
		try {
			connection = factory.newConnection();
			connection.addShutdownListener(this); // 예기치 않은 일이 발생하면 shutdownCompleted 함수 실행
			LOGGER.info("Connected to " + factory.getHost() + ":" + factory.getPort());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to connect to " + factory.getHost() + ":" + factory.getPort(), e);
			asyncWaitAndReconnect();
		}
	}
	
	public void stop() {
		executor.shutdownNow();
		
		if (connection == null) {
			return;
		}
		
		try {
			connection.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to close connection", e);
		} finally {
			connection = null;
		}
	}
	
	public Channel createChannel() {
		try {
			return connection == null ? null : connection.createChannel();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to create channel", e);
			return null;
		}
	}
	
	public void closeChannel(final Channel channel) {
		if ((channel == null) || (!channel.isOpen())) {
			return;
		}
		
		try {
			channel.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to close channel: " + channel, e);
		}
	}
	
	public <T> T call(final ChannelCallable<T> callable) {
		final Channel channel = createChannel();
		if (channel != null) {
			try {
				LOGGER.info("Call to " + channel.toString());
				return callable.call(channel);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Failed to run: " + callable.getDescription() + " on channel: " + channel, e);
			} finally {
				closeChannel(channel);
			}
		}
		return null;
	}
}
