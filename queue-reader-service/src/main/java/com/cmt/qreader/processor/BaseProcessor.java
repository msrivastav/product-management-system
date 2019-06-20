package com.cmt.qreader.processor;

/**
 * This interface is to be implemented by all the processors.
 * @author Manoo.Srivastav
 */
@FunctionalInterface
public interface BaseProcessor {
	public void process(Object object);
}
