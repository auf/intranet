package com.savoirfairelinux.auf.portlet.annuaire;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.savoirfairelinux.auf.hook.db.AufCountry;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.db.AufImplantation;
import com.savoirfairelinux.auf.hook.db.AufRegion;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;
import com.savoirfairelinux.auf.portlet.AufCombinedUser;

@Controller
@RequestMapping("VIEW")
public class AufAnnuaire {

	@RenderMapping
	public String view(Model model) throws Exception {
		model.addAttribute("displaySearch", false);
		model.addAttribute("displayProfile", false);
		
		initializeSearchFields(model);
		
		return "view";
	}

	private void initializeSearchFields(Model model) {
		List<AufImplantation> implantations = AnnuaireUtil.getAllImplantations();
		model.addAttribute("implantations", implantations);
		
		List<AufCountry> countries = AnnuaireUtil.getAllCountries();
		model.addAttribute("countries", countries);
		
		List<AufRegion> regions = AnnuaireUtil.getAllRegions();
		model.addAttribute("regions", regions);
		
		List<String> cities = AnnuaireUtil.getAllCities();
		model.addAttribute("cities", cities);
		
		List<String> types = AnnuaireUtil.getAllTypes();
		model.addAttribute("types", types);
	}

	@RenderMapping(params = "action=searchPerson")
	public String search(Model model, RenderRequest request,
			RenderResponse response) throws Exception {
		String name = ParamUtil.getString(request, "name", null);
		if (name.equals("")) name = null;
		long implantation = ParamUtil.getLong(request, "implantation", -1);
		String type = ParamUtil.getString(request, "type", null);
		if (type.equals("")) type = null;
		String city = ParamUtil.getString(request, "city", null);
		if (city.equals("")) city = null;
		String country = ParamUtil.getString(request, "country", null);
		if (country.equals("")) country = null;
		long region = ParamUtil.getLong(request, "region", -1);

		//List<AufEmploye> employees = AnnuaireUtil.getUsersLike(search);
		List<AufEmploye> employees;
		if (request.getParameter("search").equals("tous")) {
			employees = AnnuaireUtil.getAllData();
		} else {
			employees = AnnuaireUtil.getUsers(name, implantation, type, city, country, region);
		}

		model.addAttribute("size", employees.size());
		
		initializeSearchFields(model);
		
		//initialize list with user portraits
		List<AufCombinedUser> users = new LinkedList<AufCombinedUser>();
		ThemeDisplay themeDisplay= (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		
		for (AufEmploye e : employees) {
			if ((e != null) && (e.getEmail()!= null)) {
				try {
					User liferayUser = UserLocalServiceUtil.getUserByEmailAddress(CompanyThreadLocal.getCompanyId(), e.getEmail());
					users.add(new AufCombinedUser(e, liferayUser.getPortraitURL(themeDisplay)));
				} catch (NoSuchUserException ex) {
					System.out.println("MISSED USER");
				}
			}
		}
		model.addAttribute("users", users);

		model.addAttribute("displaySearch", true);
		model.addAttribute("displayProfile", false);
		return "view";

	}

	@RenderMapping(params = "action=viewPerson")
	public String viewPerson(Model model, RenderRequest request,
			RenderResponse response) throws Exception {
		String email = ParamUtil.getString(request, "idEmploye", "");

		AufEmploye user = null;

		user = AnnuaireUtil.getUserByEmail(email);
		if (user == null) {
			throw new PortalException("No user found for the email address: "
					+ email);
		}
		
		User liferayUser = UserLocalServiceUtil.getUserByEmailAddress(CompanyThreadLocal.getCompanyId(), email);
		
		List<Group> userSites = new ArrayList<Group>();
		for (Group site : liferayUser.getGroups()) {
		  if (site.isRegularSite()) {
			  userSites.add(site);
		  }
		}
		
		List<Group> userPoles = new ArrayList<Group>();
		for (Organization org : liferayUser.getOrganizations()) {
		  if (org.hasPublicLayouts()) {
			  userPoles.add(org.getGroup());
		  }
		}
		
		ThemeDisplay themeDisplay= (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		
		model.addAttribute("userAnnuaire", user);
		model.addAttribute("userSites", userSites);
		model.addAttribute("userPoles", userPoles);
		model.addAttribute("userPortraitUrl", liferayUser.getPortraitURL(themeDisplay));

		if (liferayUser.getExpandoBridge().hasAttribute("projets")) {
			if (liferayUser.getExpandoBridge().getAttribute("projets") != null) {
				model.addAttribute("userProjects", liferayUser.getExpandoBridge().getAttribute("projets").toString());
			} else {
				model.addAttribute("userProjects", "");
			}
		} else {
			model.addAttribute("userProjects", "NO PROJECTS FIELD FOUND");
		}
			
		if (liferayUser.getExpandoBridge().hasAttribute("interets")) {
			if (liferayUser.getExpandoBridge().getAttribute("interets") != null) {
				model.addAttribute("userInterests", liferayUser.getExpandoBridge().getAttribute("interets").toString());
			} else {
				model.addAttribute("userInterests", "");
			}
		} else {
			model.addAttribute("userInterests", "NO INTEREST FIELD FOUND");
		}
		
		HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request));
		PortalUtil.addPortletBreadcrumbEntry(httpRequest, "Fiche employé : " + user.getFullName(), PortalUtil.getCurrentURL(request));
		
		model.addAttribute("displaySearch", false);
		model.addAttribute("displayProfile", true);
		return "view";

	}

}
