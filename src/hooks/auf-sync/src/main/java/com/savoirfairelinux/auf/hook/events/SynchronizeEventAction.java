package com.savoirfairelinux.auf.hook.events;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserConstants;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.UserAttributes;
import com.liferay.portlet.usersadmin.util.UsersAdmin;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;

public class SynchronizeEventAction extends Action {
	
	private static final Log log = LogFactoryUtil.getLog(SynchronizeEventAction.class);

	@Override
	public void run(HttpServletRequest req, HttpServletResponse res)
			throws ActionException {

		List<AufEmploye> results = AnnuaireUtil.getAllData();	
			
		for(AufEmploye aufEmploye : results) {
			if (aufEmploye.getLogin() == null) continue;
			
			long companyId = CompanyThreadLocal.getCompanyId();
			User liferayUser = null;
			try {
				liferayUser = UserLocalServiceUtil.getUserByEmailAddress(companyId, aufEmploye.getEmail());
			} catch (PortalException portalException) {
				// this exception is thrown if no user can be found
				// create new user
				try {
					liferayUser = UserLocalServiceUtil.addUser(
							UserLocalServiceUtil.getDefaultUserId(companyId),
							companyId,
							true,
							"",
							"",
							false,
							aufEmploye.getLogin(),
							aufEmploye.getEmail(),
							0,
							"",
							new Locale("fr"),
							aufEmploye.getFirstName(),
							"",
							aufEmploye.getLastName(),
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
					
				} catch (Exception ex) {
					log.error("User could not be created");
					ex.printStackTrace();
				}
			} catch (SystemException e1) {
				e1.printStackTrace();
				return;
			}
			
			liferayUser.setScreenName(aufEmploye.getLogin()); //has to be unique in the DB because of indexes
			liferayUser.setEmailAddress(aufEmploye.getEmail()); //has to be unique in the DB because of indexes
			liferayUser.setCompanyId(companyId);
			liferayUser.setFirstName(aufEmploye.getFirstName());
			liferayUser.setLastName(aufEmploye.getLastName());				
			liferayUser.setScreenName(aufEmploye.getLogin());
			liferayUser.setStatus(0); //0 = active
			
			try {
				String name = aufEmploye.getImplantation().getRegion().getName();
				long orgId = OrganizationLocalServiceUtil.getOrganizationId(companyId, name);
				if (orgId != 0) {
					UserLocalServiceUtil.addOrganizationUsers(orgId, new long[] {liferayUser.getUserId()});
				}
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}

			try {
				liferayUser.persist();
			} catch (SystemException e1) {
				log.error("Could not persist employe: " + liferayUser);
				e1.printStackTrace();
			}

		}


	}

}