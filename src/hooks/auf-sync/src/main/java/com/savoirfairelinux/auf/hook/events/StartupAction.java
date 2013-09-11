package com.savoirfairelinux.auf.hook.events;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.CountryConstants;
import com.liferay.portal.model.ListTypeConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.RegionConstants;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.savoirfairelinux.auf.hook.db.AufRegion;

public class StartupAction extends SimpleAction {
	
	private static final Log log = LogFactoryUtil.getLog(StartupAction.class);

	@Override
	public void run(String[] ids) throws ActionException {
		log.info("Adding Organisations");
		
		log.info("Adding AUF");
		Organization auf = addOrganization("AUF", null);
		
		log.info("Adding Poles");
		Organization poleStrategic = addOrganization("Pôles stratégiques", auf);
		addOrganization("A1", poleStrategic);
		addOrganization("A2", poleStrategic);
		addOrganization("A3", poleStrategic);
		addOrganization("B1", poleStrategic);
		addOrganization("B2", poleStrategic);
		addOrganization("B3", poleStrategic);
		
		log.info("Adding Strategic Poles");
		Organization poleSupport = addOrganization("Pôles support", auf);
		addOrganization("Finance", poleSupport);
		addOrganization("Informatique", poleSupport);
		addOrganization("Institutionnel, juridique et communication", poleSupport);
		addOrganization("Ressources humaines", poleSupport);
		addOrganization("Services généraux", poleSupport);
		
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AufDatabase");
        EntityManager entityManager = emf.createEntityManager();
        List<AufRegion> regions = new ArrayList<AufRegion>();
        regions = entityManager.createNamedQuery("AufRegion.findAll", AufRegion.class).getResultList();
		
		log.info("Adding Regions");
		Organization poleRegions = addOrganization("Régions", auf);
		for(AufRegion region : regions) {
			addOrganization(region.getName(), poleRegions);
		}

	}

	private Organization addOrganization(String name, Organization parent) {
		long parent_id = OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID;
		if (parent != null) {
			parent_id = parent.getOrganizationId();
		}
		
		try {
			return OrganizationLocalServiceUtil.getOrganization(CompanyThreadLocal.getCompanyId(), name);
		} catch (PortalException e1) {
			try {
				return OrganizationLocalServiceUtil.addOrganization(
						UserLocalServiceUtil.getDefaultUser(CompanyThreadLocal.getCompanyId()).getUserId(),
						parent_id,
						name,
						OrganizationConstants.TYPE_REGULAR_ORGANIZATION,
						true,
						RegionConstants.DEFAULT_REGION_ID,
						CountryConstants.DEFAULT_COUNTRY_ID,
						ListTypeConstants.ORGANIZATION_STATUS_DEFAULT,
						"",
						true,
						null);
			} catch (PortalException e) {
				log.error("Error while creating organization: " + name);
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}