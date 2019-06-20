package com.cmt.qreader.reader.receiver;

/**
 * Interface to be implemented by all message receivers
 * @author Manoo.Srivastav
 */
@FunctionalInterface
public interface BaseMessageReceiver {
	public void receiverMsg(Object object);
}
