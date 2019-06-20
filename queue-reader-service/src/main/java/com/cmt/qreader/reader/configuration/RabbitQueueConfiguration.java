package com.cmt.qreader.reader.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This file keeps the configurations for rabbit MQ
 * @author Manoo.Srivastav
 */
@Component
public class RabbitQueueConfiguration {

	@Value("${queue.rabbit.name}")
	private String queueName;
	
	@Value("${exchange.rabbit.name}")
	private String exchangeName;
	
	@Bean
	Queue ordersQueue() {
		return QueueBuilder.durable(queueName).build();
	}

	@Bean
	Exchange ordersExchange() {
		return ExchangeBuilder.topicExchange(exchangeName).build();
	}

	@Bean
	Binding binding(Queue ordersQueue, TopicExchange ordersExchange) {
		return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(queueName);
	}
	
	/*
	 * This bean is to customize min and max number of threads to be consumers
	 */
	@Value("${consumer.thread.min}")
	private Integer minConsumers; 
	
	@Value("${consumer.thread.max}")
	private Integer maxConsumers; 
	
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
	    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory);
	    factory.setConcurrentConsumers(minConsumers);
	    factory.setMaxConcurrentConsumers(maxConsumers);
	    return factory;
	}
}
