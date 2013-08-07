package com.savoirfairelinux.auf.hook.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.savoirfairelinux.auf.hook.db.AufEmployeTable;

public class AnnuaireUtil {
	public static AnnuaireUtil instance = new AnnuaireUtil();
    
    private EntityManager entityManager;
//    private Log log = LogFactoryUtil.getLog(getClass());

    private AnnuaireUtil() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AufDatabase");
        entityManager = emf.createEntityManager();
    }

    public static List<AufEmployeTable> getAllData() throws Exception {
        List<AufEmployeTable> result = new ArrayList<AufEmployeTable>();
        try {
            result = instance.entityManager.createNativeQuery("SELECT * FROM ref_employe", AufEmployeTable.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
