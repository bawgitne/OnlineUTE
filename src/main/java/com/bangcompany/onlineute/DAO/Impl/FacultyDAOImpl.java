
package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.FacultyDAO;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FacultyDAOImpl implements FacultyDAO {
    @Override
    public List<Faculty> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy toàn bộ danh sách khoa sắp xếp theo tên
            return em.createQuery("SELECT f FROM Faculty f ORDER BY f.fullName", Faculty.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Faculty save(Faculty faculty) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (faculty.getId() == null) {
                // Thêm mới khoa
                em.persist(faculty);
            } else {
                // Cập nhật thông tin khoa
                faculty = em.merge(faculty);
            }
            em.getTransaction().commit();
            return faculty;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Faculty faculty = em.find(Faculty.class, id);
            if (faculty != null) {
                // Xóa khoa nếu tồn tại
                em.remove(faculty);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
