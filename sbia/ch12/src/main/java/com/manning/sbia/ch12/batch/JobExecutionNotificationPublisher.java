package com.manning.sbia.ch12.batch;

import javax.management.Notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

public class JobExecutionNotificationPublisher implements ApplicationListener, NotificationPublisherAware {

	protected static final Log logger = LogFactory.getLog(JobExecutionNotificationPublisher.class);

	private NotificationPublisher notificationPublisher;

	private int notificationCount = 0;

	/**
	 * Injection setter.
	 * 
	 * @see org.springframework.jmx.export.notification.NotificationPublisherAware#setNotificationPublisher(org.springframework.jmx.export.notification.NotificationPublisher)
	 */
	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}

	/**
	 * If the event is a {@link SimpleMessageApplicationEvent} for open and
	 * close we log the event at INFO level and send a JMX notification if we
	 * are also an MBean.
	 * 
	 * @see ApplicationListener#onApplicationEvent(ApplicationEvent) 
	 */
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof SimpleMessageApplicationEvent) {
			String message = applicationEvent.toString();
			logger.info(message);
			publish(message);
		}
	}

	/**
	 * Publish the provided message to an external listener if there is one.
	 * 
	 * @param message the message to publish
	 */
	private void publish(String message) {
		if (notificationPublisher != null) {
			Notification notification = new Notification("JobExecutionApplicationEvent", this, notificationCount++,
					message);
			/*
			 * We can't create a notification with a null source, but we can set
			 * it to null after creation(!). We want it to be null so that
			 * Spring will replace it automatically with the ObjectName (in
			 * ModelMBeanNotificationPublisher).
			 */
			notification.setSource(null);
			notificationPublisher.sendNotification(notification);
		}
	}

}