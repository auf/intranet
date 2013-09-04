package com.savoirfairelinux.auf.hook.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.savoirfairelinux.auf.hook.db.AufEmploye;

public class AnnuaireUtil {
	public static AnnuaireUtil instance = new AnnuaireUtil();
    
    private EntityManager entityManager;

    private AnnuaireUtil() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AufDatabase");
        entityManager = emf.createEntityManager();
    }

    public static List<AufEmploye> getAllData() {
        List<AufEmploye> result = new ArrayList<AufEmploye>();
        try {
            result = instance.entityManager.createNativeQuery("SELECT * FROM ref_employe WHERE courriel IS NOT NULL", AufEmploye.class)
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

}
