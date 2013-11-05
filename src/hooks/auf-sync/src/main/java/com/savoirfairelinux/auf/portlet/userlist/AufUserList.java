package com.savoirfairelinux.auf.portlet.userlist;

import java.util.LinkedList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;
import com.savoirfairelinux.auf.portlet.AufCombinedUser;

@Controller
@RequestMapping("VIEW")
public class AufUserList {

	@RenderMapping
	public String view(Model model, RenderRequest request, RenderResponse response) throws Exception {
		
		ThemeDisplay themeDisplay= (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		Group currentGroup = themeDisplay.getLayout().getGroup();
		
		List<User> users;
		if (currentGroup.isOrganization()) {
			users = UserLocalServiceUtil.getOrganizationUsers(currentGroup.getOrganizationId());
		} else if (currentGroup.isRegularSite()) {
			users = UserLocalServiceUtil.getGroupUsers(currentGroup.getGroupId());
		} else {
			users = null;
		}
		
		
		if (users != null) {
			//create the auf employe list for them
			List<AufEmploye> employees = AnnuaireUtil.getUsers(users);
			
			//put them in the template
			List<AufCombinedUser> combinedUsers = new LinkedList<AufCombinedUser>();
			
			for (AufEmploye e : employees) {
				if ((e != null) && (e.getEmail()!= null)) {
					try {
						User liferayUser = UserLocalServiceUtil.getUserByEmailAddress(CompanyThreadLocal.getCompanyId(), e.getEmail());
						combinedUsers.add(new AufCombinedUser(e, liferayUser.getPortraitURL(themeDisplay)));
					} catch (NoSuchUserException ex) {
						System.out.println("MISSED USER");
					}
				}
			}
			model.addAttribute("users", combinedUsers);
		}
		return "view";
	}
}
