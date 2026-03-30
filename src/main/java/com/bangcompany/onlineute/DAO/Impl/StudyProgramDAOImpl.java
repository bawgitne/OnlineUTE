package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.StudyProgramDAO;
import com.bangcompany.onlineute.Model.Entity.StudyProgram;
import jakarta.persistence.EntityManager;
import java.util.List;

public class StudyProgramDAOImpl implements StudyProgramDAO {
    @Override
    public List<StudyProgram> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM StudyProgram s", StudyProgram.class).getResultList();
        } finally {
            em.close();
        }
    }
}
