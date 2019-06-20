package com.cmt.importer.writer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is configuration class for RabbitMQ
 * Queue, exchange are being via docker file
 * @author Manoo.Srivastav
 */

@Configuration
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
}
