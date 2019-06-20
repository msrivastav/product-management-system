package com.cmt.qreader.reader.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cmt.qreader.processor.BaseProcessor;

import lombok.extern.slf4j.Slf4j;
import pojo.Product;

@Component
@Slf4j
public class QueueMessageReceiver implements BaseMessageReceiver {

	@Autowired
	@Qualifier("productMessageProcessor")
	private BaseProcessor dbProcessor;
	
	@RabbitListener(queues = ("${queue.rabbit.name}"))
	public void processMsg(Product product) {
		receiverMsg(product);
	}

	@Override
	public void receiverMsg(Object object) {
		log.info("Message from queue : " + object);
		dbProcessor.process(object);
	}

}
