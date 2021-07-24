package com.skok1025.study.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Queue.BindOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

public class UserMessageManager {
	
	static final String USER_INBOXES_EXCHANGE = "user_inboxes";
	
	private RabbitMqManager rabbitMqManager;
	
	public UserMessageManager(RabbitMqManager rabbitMqManager) {
		this.rabbitMqManager = rabbitMqManager;
	}
	
	public void onApplicationStart() {
		rabbitMqManager.call(new ChannelCallable<DeclareOk>() {

			@Override
			public String getDescription() {
				return "Declaring direct exchange: " + USER_INBOXES_EXCHANGE;
			}

			@Override
			public DeclareOk call(Channel channel) throws IOException {
				String exchange = USER_INBOXES_EXCHANGE;
				String type = "direct";
				
				// 서버 재시작 후에도 살아 남는다. 
				boolean durable = true;
				
				// 사용자가 없더라도 유지시킨다. 
				boolean autoDelete = false;
				
				// 특별한 인자 값은 없다.
				Map<String, Object> arg = null;
				
				return channel.exchangeDeclare(exchange, type, durable, autoDelete, arg);
			}
			
		});
	}
	
	public void onUserLogin(final long userId) {
		final String queue = getUserInboxQueue(userId);
		
		rabbitMqManager.call(new ChannelCallable<BindOk>() {

			@Override
			public String getDescription() {
				return "Declaring user queue: " + queue + ", binding it to exchange: " + USER_INBOXES_EXCHANGE;
			}

			@Override
			public BindOk call(Channel channel) throws IOException {
				return declareUserMessageQueue(queue, channel);
			}

			private BindOk declareUserMessageQueue(String queue, Channel channel) throws IOException{
				boolean durable = true;
				boolean autoDelete = false;
				// 다른 연결이 소비할 수 있다.
				boolean exclusive = false;
				
				Map<String, Object> arg = null;
				channel.queueDeclare(queue, durable, exclusive, autoDelete, arg);
				String routingKey = queue;
				
				return channel.queueBind(queue, USER_INBOXES_EXCHANGE, routingKey);
			}
		});
	}

	private String getUserInboxQueue(long userId) {
		return "user-inbox." + userId;
	}
	
	// 사용자에게 메시지 보내기
	static final String MESSAGE_CONTENT_TYPE = "application/vnd.ccm.pmsg.vl+json";
	static final String MESSAGE_ENCODING = "UTF-8";
	
	public String sendUserMessage(final long userId, final String jsonMessage) {
		return rabbitMqManager.call(new ChannelCallable<String>() {

			@Override
			public String getDescription() {
				return "Sending message to user: " + userId;
			}

			@Override
			public String call(Channel channel) throws IOException {
				String queue = getUserInboxQueue(userId);
				declareUserMessageQueue(queue, channel);
				String messageId = UUID.randomUUID().toString();
				BasicProperties props = new BasicProperties.Builder()
						.contentType(MESSAGE_CONTENT_TYPE)
						.contentEncoding(MESSAGE_ENCODING)
						.messageId(messageId)
						.deliveryMode(2) // 1- 비영속성, 2- 영속성
						.build();
				
				String routingKey = queue;
				
				channel.basicPublish(USER_INBOXES_EXCHANGE, routingKey, props, jsonMessage.getBytes(MESSAGE_ENCODING));
				return messageId;
			}

			private BindOk declareUserMessageQueue(String queue, Channel channel) throws IOException {
				boolean durable = true;
				boolean autoDelete = false;
				// 다른 연결이 소비할 수 있다.
				boolean exclusive = false;
				
				Map<String, Object> arg = null;
				channel.queueDeclare(queue, durable, exclusive, autoDelete, arg);
				String routingKey = queue;
				
				return channel.queueBind(queue, USER_INBOXES_EXCHANGE, routingKey);
			}
			
		});
	}
	
	public List<String> fetchUserMessages(final long userId) {
		return rabbitMqManager.call(new ChannelCallable<List<String>>() {

			@Override
			public String getDescription() {
				return "Fetching messages for user: " + userId;
			}

			@Override
			public List<String> call(Channel channel) throws IOException {
				List<String> messages = new ArrayList<String>();
				String queue = getUserInboxQueue(userId);
				boolean autoAck = true;
				GetResponse getResponse;
				while ((getResponse = channel.basicGet(queue, autoAck)) != null) {
					final String contentEncoding = getResponse.getProps().getContentEncoding();
					messages.add(new String(getResponse.getBody(), contentEncoding));
				}
				return messages;
			}
			
		});
	}

}
