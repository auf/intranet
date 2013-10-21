package com.savoirfairelinux.auf.hook.events;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.DuplicateUserScreenNameException;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.util.AnnuaireUtil;


public class SynchronizeEventAction extends Action {
	
	private static final Log log = LogFactoryUtil.getLog(SynchronizeEventAction.class);

	@Override
	public void run(HttpServletRequest req, HttpServletResponse res)
			throws ActionException {

		synchronizeUsers();

	}

	public static void synchronizeUsers() {
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
				boolean errorCreatingUser = true;
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
							new Locale("fr_CA"),
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
					errorCreatingUser = false;
					
				} catch (DuplicateUserScreenNameException e) {
					log.error("AufEmploye : " + aufEmploye.toString());
					e.printStackTrace();
				} catch (PortalException e) {
					log.error("User could not be created");
					e.printStackTrace();
				} catch (SystemException e) {
					log.error("User could not be created");
					e.printStackTrace();
				}
				if (errorCreatingUser) continue;
			} catch (SystemException e1) {
				e1.printStackTrace();
				return;
			}
			
			liferayUser.setScreenName(aufEmploye.getLogin()); //has to be unique in the DB because of indexes
			liferayUser.setEmailAddress(aufEmploye.getEmail()); //has to be unique in the DB because of indexes
			liferayUser.setCompanyId(companyId);
			liferayUser.setFirstName(aufEmploye.getFirstName());
			liferayUser.setLastName(aufEmploye.getLastName());
			liferayUser.setStatus(0); //0 = active
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.newDate());
			calendar.add(Calendar.SECOND, (-7)*86400);
			Date lastWeek = calendar.getTime();
			if (liferayUser.getLastLoginDate() == null || (!liferayUser.getLastLoginDate().after(lastWeek))) {
				liferayUser.setPassword("Random4ByAFairDice");
				liferayUser.setPasswordModified(true);
				liferayUser.setPasswordModifiedDate(DateUtil.newDate());
			}
			
			//if no picture exists yet, try to find one
			if (liferayUser.getPortraitId() == 0) {
				String portraitPath = "/opt/liferay-portal/auf/images";
				try {
					portraitPath = PrefsPropsUtil.getString("auf.portrait.path");
				} catch (SystemException e1) {
					log.info("auf.portrait.path was not set");
					e1.printStackTrace();
				}
				String filePath = portraitPath + "/d-0340-" + aufEmploye.getId() +"-photo.jpg";
				
				if (FileUtil.exists(portraitPath)) {
					log.info("auf.portrait.path=\"" + portraitPath + "\" found");
					log.info("The directory contains following files");
					for (String f : FileUtil.listFiles(portraitPath)) {
						log.info(f);
					}
				} else {
					log.error("auf.portrait.path=\"" + portraitPath + "\" folder not found or not accesible");
				}
				
				if (FileUtil.exists(filePath)) {
					byte[] image = null;
					try {
						image = FileUtil.getBytes(new File(filePath));
						RenderedImage portrait = ImageToolUtil.read(image).getRenderedImage();
						if (portrait.getHeight() > PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_HEIGHT) || portrait.getWidth() > PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_WIDTH)) {
							portrait = ImageToolUtil.scale(portrait, PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_HEIGHT), PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_WIDTH));
							image = ImageToolUtil.getBytes(portrait, ImageToolUtil.read(image).getType());
						}
					} catch (IOException e) {
						//the image could not be read
					} catch (SystemException e) {
						e.printStackTrace();
					}
					
					try {
						if ((image != null) && (image.length > 0)) {
							UserLocalServiceUtil.updatePortrait(liferayUser.getUserId(), image);
						}
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
				} else {
					log.info("No portrait was found for: " + aufEmploye.getId());
				}

			}
			
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