package com.cmt.importer.writer.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("queueMessageSender")
@Slf4j
public class QueueMessageSender implements BaseMessageSender{

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${queue.rabbit.name}")
	private String routingKey;

	
	@Override
	public void sendMsg(Object product) {
		log.info("Sending message to queue : {}", product);
		try {
			this.rabbitTemplate.convertAndSend(routingKey, product);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
