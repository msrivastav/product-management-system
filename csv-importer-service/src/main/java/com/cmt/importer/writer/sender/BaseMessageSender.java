package com.cmt.importer.writer.sender;

/**
 * Base interface to be implemented by all message senders
 * @author Manoo.Srivastav
 */
@FunctionalInterface
public interface BaseMessageSender {
	public void sendMsg(Object object);
}
