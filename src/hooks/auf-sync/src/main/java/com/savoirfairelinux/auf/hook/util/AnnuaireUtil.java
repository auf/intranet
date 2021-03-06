package com.savoirfairelinux.auf.hook.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.liferay.portal.model.User;
import com.savoirfairelinux.auf.hook.db.AufCountry;
import com.savoirfairelinux.auf.hook.db.AufEmploye;
import com.savoirfairelinux.auf.hook.db.AufImplantation;
import com.savoirfairelinux.auf.hook.db.AufRegion;

public class AnnuaireUtil {
	public static AnnuaireUtil instance = new AnnuaireUtil();
    
    private EntityManager entityManager;

    private AnnuaireUtil() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AufDatabase");
        entityManager = emf.createEntityManager();
    }
    
    public static AnnuaireUtil getInstance() {
    	return instance;
    }

    public static List<AufEmploye> getAllData() {
        List<AufEmploye> result = new ArrayList<AufEmploye>();
        try {
            result = instance.entityManager.createNamedQuery("AufEmploye.findAll", AufEmploye.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	public static List<AufEmploye> getUsersLike(String search) {
		List<AufEmploye> result = new ArrayList<AufEmploye>();
		search = "%" + search + "%";
        try {
            result = instance.entityManager.createNamedQuery("AufEmploye.findLikeName", AufEmploye.class)
            		.setParameter("param", search.toLowerCase())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}

	public static AufEmploye getUserByEmail(String email) {
		AufEmploye user = null;
        try {
        	user = instance.entityManager.createNamedQuery("AufEmploye.findByEmail", AufEmploye.class)
            		.setParameter("email", email.toLowerCase())
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
	}

	public static List<AufImplantation> getAllImplantations() {
		List<AufImplantation> result = new ArrayList<AufImplantation>();
		result = instance.entityManager.createNamedQuery("AufImplantation.findAll", AufImplantation.class).getResultList();
        return result;
	}
	
	public static List<String> getAllCities() {
		List<String> result = new ArrayList<String>();
		result = instance.entityManager.createNamedQuery("AufImplantation.findAllCities", String.class).getResultList();
        return result;
	}
	
	public static List<AufCountry> getAllCountries() {
		List<AufCountry> result = new ArrayList<AufCountry>();
		result = instance.entityManager.createNamedQuery("AufCountry.findAll", AufCountry.class).getResultList();
        return result;
	}
	
	public static List<AufRegion> getAllRegions() {
		List<AufRegion> result = new ArrayList<AufRegion>();
		result = instance.entityManager.createNamedQuery("AufRegion.findAll", AufRegion.class).getResultList();
        return result;
	}

	public static List<String> getAllTypes() {
		List<String> result = new ArrayList<String>();
		result = instance.entityManager.createNamedQuery("AufImplantation.findAllTypes", String.class).getResultList();
        return result;
	}

	public static List<AufEmploye> getUsers(String search, long implantation,
			String type, String city, String country, long region) {
		List<AufEmploye> result = new ArrayList<AufEmploye>();
		
		List<String> conditions = new ArrayList<String>();
		StringBuilder queryString= new StringBuilder();
		queryString.append("SELECT e FROM AufEmploye e WHERE ");
		conditions.add("e.active=1");
		
		if (search != null) {
			conditions.add("(LOWER(e.lastName) LIKE :search"
					+ " OR LOWER(e.firstName) LIKE :search"
					+ " OR LOWER(e.postDesc) LIKE :search"
					+ " OR LOWER(e.telPost) LIKE :search"
					+ " OR LOWER(e.telIp) LIKE :search"
					+ " OR LOWER(e.telIpNomade) LIKE :search"
					+ " OR LOWER(e.implantation.name) LIKE :search"
					+ " OR LOWER(e.implantation.city) LIKE :search"
					+ " OR LOWER(e.service.name) LIKE :search)");
		}
		if (implantation != -1) {
			conditions.add("e.physicalImplantation.id = :implantation");
		}
		if (type != null) {
			conditions.add("e.physicalImplantation.type = :type");
		}
		if (city != null) {
			conditions.add("e.physicalImplantation.city = :city");
		}
		if (country != null) {
			conditions.add("e.physicalImplantation.country = :country");
		}
		if (region != -1) {
			conditions.add("e.physicalImplantation.region.id = :region");
		}
		
		for (int i = 0; i < conditions.size(); i++) {
	        if (i > 0) queryString.append(" AND ");
	        queryString.append(conditions.get(i));
	    }
		queryString.append(" ORDER BY e.lastName, e.firstName ");
		TypedQuery<AufEmploye> query = instance.entityManager.createQuery(queryString.toString(), AufEmploye.class);
		
		if (search != null) {
			query = query.setParameter("search", "%" + search.toLowerCase() + "%");
		}
		if (implantation != -1) {
			query = query.setParameter("implantation", implantation);
		}
		if (type != null) {
			query = query.setParameter("type", type);
		}
		if (city != null) {
			query = query.setParameter("city", city);
		}
		if (country != null) {
			query = query.setParameter("country", country);
		}
		if (region != -1) {
			query = query.setParameter("region", region);
		}
		result = query.getResultList();
        return result;
	}

	public static List<AufEmploye> getUsers(List<User> users) {
		List<AufEmploye> result = new ArrayList<AufEmploye>();
		List<String> emails = new ArrayList<String>();
		
		for (User u : users) {
			emails.add(u.getEmailAddress().toLowerCase());
		}
		
		if (emails.size() > 0) {
			result = instance.entityManager.createNamedQuery("AufEmploye.findByEmails", AufEmploye.class)
            		.setParameter("emails", emails)
                    .getResultList();
		}

        return result;
	}
	
	public static String getPostForEmail(String email) {
		List<AufEmploye> result;
		
		result = instance.entityManager.createNamedQuery("AufEmploye.findByEmail", AufEmploye.class)
        		.setParameter("email", email)
                .getResultList();
		
		if (result != null && result.size()>0) {
			return result.get(0).getPostDesc();
		}
		
		return "";
		
	}

}
