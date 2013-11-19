package com.savoirfairelinux.auf.hook.events;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
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
	
	private static String generateRandomString() {
		Random rng = new Random();
		int length = 12;
		String characters = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}

	public synchronized static void synchronizeUsers() {
		List<AufEmploye> results = AnnuaireUtil.getAllData();
		
		String portraitPath = "/opt/liferay-portal/auf/images";
		try {
			portraitPath = PrefsPropsUtil.getString("auf.portrait.path");
		} catch (SystemException e1) {
			log.info("auf.portrait.path was not set");
			e1.printStackTrace();
		}
		
		if (FileUtil.exists(portraitPath)) {
			File portraitFolder = new File(portraitPath);
			log.debug("auf.portrait.path=\"" + portraitPath + "\" found - " + portraitFolder.canRead() + " - " + portraitFolder.canExecute());
			log.debug("The directory contains following files");
			for (File f : portraitFolder.listFiles()) {
				log.debug(f.getName() + " - " + f.canRead());
			}
		} else {
			log.error("auf.portrait.path=\"" + portraitPath + "\" folder not found or not accesible");
		}
			
		for(AufEmploye aufEmploye : results) {
			if (aufEmploye.getLogin() == null) continue;
			
			long companyId = CompanyThreadLocal.getCompanyId();
			try {
				companyId = CompanyLocalServiceUtil.getCompanyByWebId("intranet.auf.org").getCompanyId();
			} catch (PortalException e2) {
				e2.printStackTrace();
			} catch (SystemException e2) {
				e2.printStackTrace();
			}
			
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
					log.error("User probably changed and can no longer be synced : " + aufEmploye.toString());
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
			liferayUser.setPasswordReset(false);
			
			try {
				Contact liferayContact = liferayUser.getContact();
				liferayContact.setMale(aufEmploye.isMale());
				//TODO no data available in the reference database
				//liferayContact.setBirthday(aufEmploye.getBirthday());
				liferayContact.persist();
			} catch (PortalException e2) {
				e2.printStackTrace();
			} catch (SystemException e2) {
				e2.printStackTrace();
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.newDate());
			calendar.add(Calendar.SECOND, (-7)*86400);
			Date lastWeek = calendar.getTime();
			if (liferayUser.getLastLoginDate() == null || (!liferayUser.getLastLoginDate().after(lastWeek))) {
				liferayUser.setPassword(generateRandomString());
				liferayUser.setPasswordModified(true);
				liferayUser.setPasswordModifiedDate(DateUtil.newDate());
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
			
			//if no picture exists yet, try to find one
			if (liferayUser.getPortraitId() == 0) {
				
				File filePath = new File(portraitPath + "/d-0340-" + aufEmploye.getId() +"-photo.jpg");
				
				if (filePath.exists()) {
					byte[] image = null;
					try {
						image = FileUtil.getBytes(filePath);
						log.info("read file : " + filePath.getAbsolutePath());
						RenderedImage portrait = ImageToolUtil.read(image).getRenderedImage();
						if (portrait.getHeight() > PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_HEIGHT) || portrait.getWidth() > PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_WIDTH)) {
							portrait = ImageToolUtil.scale(portrait, PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_HEIGHT), PrefsPropsUtil.getInteger(PropsKeys.USERS_IMAGE_MAX_WIDTH));
							image = ImageToolUtil.getBytes(portrait, ImageToolUtil.read(image).getType());
							log.info("image data scaled down");
						}
					} catch (IOException e) {
						//the image could not be read
						log.error("the image could not be read");
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
					
					try {
						if ((image != null) && (image.length > 0)) {
							UserLocalServiceUtil.updatePortrait(liferayUser.getUserId(), image);
							log.info("added portrait : " + aufEmploye.getId());
						} else {
							log.info("portrait image was not valid");
						}
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
				} else {
					log.debug("No portrait was found for: " + aufEmploye.getId());
				}

			}
			
		}
	}


}