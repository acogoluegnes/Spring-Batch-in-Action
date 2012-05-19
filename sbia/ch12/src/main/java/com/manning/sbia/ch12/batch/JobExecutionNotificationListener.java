package com.manning.sbia.ch12.batch;

import javax.management.Notification;
import javax.management.NotificationListener;

public class JobExecutionNotificationListener implements NotificationListener {

	public void handleNotification(Notification notification, Object handback) {
		System.out.println("notified");
	}

}
