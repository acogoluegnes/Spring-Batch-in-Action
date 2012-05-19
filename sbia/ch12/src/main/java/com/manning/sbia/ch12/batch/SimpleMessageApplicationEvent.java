package com.manning.sbia.ch12.batch;

import org.springframework.context.ApplicationEvent;

public class SimpleMessageApplicationEvent extends ApplicationEvent {

	private String message;

	public SimpleMessageApplicationEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see java.util.EventObject#toString()
	 */
	public String toString() {
		return "message=["+message+"], " + super.toString();
	}

}