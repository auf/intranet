package com.savoirfairelinux.auf.theme.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;

public class AufSyncVelocityTool
{
	private static Log log = LogFactoryUtil.getLog(AufSyncVelocityTool.class);
	private static AufSyncVelocityTool instance;

	private AufSyncVelocityTool()
	{
	}

	public static AufSyncVelocityTool getInstance()
	{
		if (instance == null)
			instance = new AufSyncVelocityTool();
		return instance;
	}

	public String respondTest(long randomId) {
		return "Test";
	}
	
	public String getUserPost(String email) {
		log.info("getUserPost for " + email);
		return AnnuaireUtil.getPostForEmail(email);
	}

}
