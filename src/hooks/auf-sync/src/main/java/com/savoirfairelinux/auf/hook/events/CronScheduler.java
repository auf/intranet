package com.savoirfairelinux.auf.hook.events;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;

public class CronScheduler implements MessageListener {
	
	private static final Log log = LogFactoryUtil.getLog(CronScheduler.class);

	public void receive(Message arg0) throws MessageListenerException {
		log.debug("Running the user synchroinsation scheduler");
		SynchronizeEventAction.synchronizeUsers();
		log.debug("User synchroinsation scheduler finished");
	}

}
