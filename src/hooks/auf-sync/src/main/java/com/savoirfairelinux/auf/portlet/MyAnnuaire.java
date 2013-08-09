package com.savoirfairelinux.auf.portlet;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.util.ParamUtil;
import com.savoirfairelinux.auf.hook.db.AufEmployeTable;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;

@Controller
@RequestMapping("VIEW")
public class MyAnnuaire {

    @RenderMapping
    public String view(Model model) throws Exception {
    	System.out.println("DEFAULT VIEW");
    	model.addAttribute("displaySearch", false);
    	model.addAttribute("displayProfile", false);

        return "view";
    }
    
    @RenderMapping(params = "action=searchPerson")
    public String search(Model model, RenderRequest request, RenderResponse response) throws Exception {
    	System.out.println("SEARCH VIEW");
        String search = ParamUtil.getString(request, "search", "");
        
        List<AufEmployeTable> users = null;
		try {
			users = AnnuaireUtil.getUsersLike(search);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SIZE: " + users.size());
		model.addAttribute("size", users.size());
		model.addAttribute("users", users);
		
		model.addAttribute("displaySearch", true);
		model.addAttribute("displayProfile", false);
        return "view";

    }
    
    @RenderMapping(params = "action=viewPerson")
    public String viewPerson(Model model, RenderRequest request, RenderResponse response) throws Exception {
    	System.out.println("PROFILE VIEW");
        String email = ParamUtil.getString(request, "idEmploye", "");
        
        AufEmployeTable user = null;
		try {
			user = AnnuaireUtil.getUserByEmail(email);
			System.out.println(user.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("user", user);
		
		model.addAttribute("displaySearch", false);
		model.addAttribute("displayProfile", true);
        return "view";

    }

}
