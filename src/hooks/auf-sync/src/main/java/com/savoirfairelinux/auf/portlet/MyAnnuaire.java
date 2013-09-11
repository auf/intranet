package com.savoirfairelinux.auf.portlet;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;

@Controller
@RequestMapping("VIEW")
public class MyAnnuaire {

	@RenderMapping
	public String view(Model model) throws Exception {
		model.addAttribute("displaySearch", false);
		model.addAttribute("displayProfile", false);

		return "view";
	}

	@RenderMapping(params = "action=searchPerson")
	public String search(Model model, RenderRequest request,
			RenderResponse response) throws Exception {
		String search = ParamUtil.getString(request, "search", "");

		List<AufEmploye> users = AnnuaireUtil.getUsersLike(search);

		model.addAttribute("size", users.size());
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

		model.addAttribute("user", user);

		model.addAttribute("displaySearch", false);
		model.addAttribute("displayProfile", true);
		return "view";

	}

}
