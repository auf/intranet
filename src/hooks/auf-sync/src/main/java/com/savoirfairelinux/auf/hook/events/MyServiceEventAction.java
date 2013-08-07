package com.savoirfairelinux.auf.hook.events;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.savoirfairelinux.auf.hook.db.AufEmployeTable;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;

public class MyServiceEventAction extends Action {
	
	//private static final Log log = LogFactoryUtil.getLog(MyServiceEventAction.class);
	

	@Override
	public void run(HttpServletRequest req, HttpServletResponse res)
			throws ActionException {
		System.out.println("##### SERVICE EVENT LOG");
		

		List<AufEmployeTable> results = null;
		try {
			results = AnnuaireUtil.getAllData();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			
		for(AufEmployeTable e : results) {
			if (e.getLogin() == null) continue;
			
			long companyId = CompanyThreadLocal.getCompanyId();
			User employe = null;
			try {
				employe = UserLocalServiceUtil.getUserByEmailAddress(companyId, e.getEmail());
			} catch (PortalException e1) {
				// create new user
				try {
					System.out.println("CREATING NEW USER");
					employe = UserLocalServiceUtil.addUser(
							UserLocalServiceUtil.getDefaultUserId(companyId),
							companyId,
							true,
							"",
							"",
							false,
							e.getLogin(),
							e.getEmail(),
							0,
							"",
							new Locale("en"),
							e.getFirstName(),
							"",
							e.getLastName(),
							0,
							0,
							false,
							1,
							1,
							1970,
							"",
							null,
							null,
							null,
							null,
							false,
							null);
					
				} catch (SystemException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (PortalException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}					
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("MODIFYING USER");
			employe.setScreenName(e.getLogin()); //has to be unique in the DB because of indexes
			employe.setEmailAddress(e.getEmail()); //has to be unique in the DB because of indexes
			employe.setCompanyId(companyId);
			employe.setFirstName(e.getFirstName());
			employe.setLastName(e.getLastName());				
			employe.setScreenName(e.getLogin());

			try {
				System.out.println("SAVING USER");
				employe.persist();
				System.out.println("SAVED USER");
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}


	}

}