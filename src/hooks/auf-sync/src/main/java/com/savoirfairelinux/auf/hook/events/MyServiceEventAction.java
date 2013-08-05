package com.savoirfairelinux.auf.hook.events;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.UserUtil;

public class MyServiceEventAction extends Action {
	
	//private static final Log log = LogFactoryUtil.getLog(MyServiceEventAction.class);
	

	@Override
	public void run(HttpServletRequest req, HttpServletResponse res)
			throws ActionException {
		System.out.println("##### SERVICE EVENT LOG");
		
		try {
			int count = UserLocalServiceUtil.getUsersCount();
			System.out.println(count);
			List<User> users = UserLocalServiceUtil.getUsers(0, count);
			for(User u : users) {
				System.out.println(u);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		User u = UserLocalServiceUtil.createUser(0);
		u.setScreenName("mytestuser");
		u.setEmailAddress("mytestuser@example.org");
		try {
			System.out.println("USER CREATING");
			u.persist();
			System.out.println("USER CREATED");
			System.out.println(u);
			System.out.println(u.getUserId());
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println("USER CREATION FAILED");
			//e.printStackTrace();
		}
		
		try {
			User u1 = UserLocalServiceUtil.getUserByEmailAddress(10154, "test@liferay.com");
			System.out.println(u1);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}