package com.savoirfairelinux.auf.theme.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.util.WebKeys;
import com.savoirfairelinux.auf.theme.util.AufVelocityTool;

public class AufPreAction extends com.liferay.portal.kernel.events.Action
{
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException
	{	
		// theme
		@SuppressWarnings("unchecked")
		Map<String, Object> vmVariables = (Map<String, Object>) request.getAttribute(WebKeys.VM_VARIABLES);
		if (vmVariables == null) vmVariables = new HashMap<String, Object>();
		vmVariables.put("aufTool", AufVelocityTool.getInstance());
		request.setAttribute(WebKeys.VM_VARIABLES, vmVariables);
		
	}
}
