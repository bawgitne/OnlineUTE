
package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.MajorDAO;
import com.bangcompany.onlineute.Model.Entity.Major;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MajorDAOImpl implements MajorDAO {
    @Override
    public List<Major> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy toàn bộ danh sách ngành học kèm thông tin khoa và sắp xếp theo tên
            return em.createQuery(
                    "SELECT m FROM Major m JOIN FETCH m.faculty ORDER BY m.fullName",
                    Major.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Major> findByFacultyId(Long facultyId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Tìm các ngành học thuộc một khoa cụ thể
            return em.createQuery(
                    "SELECT m FROM Major m JOIN FETCH m.faculty f WHERE f.id = :facultyId ORDER BY m.fullName",
                    Major.class
            ).setParameter("facultyId", facultyId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Major save(Major major) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (major.getId() == null) {
                // Thêm mới ngành học
                em.persist(major);
            } else {
                // Cập nhật thông tin ngành học
                major = em.merge(major);
            }
            em.getTransaction().commit();
            return major;
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
            Major major = em.find(Major.class, id);
            if (major != null) {
                // Xóa ngành học nếu tồn tại
                em.remove(major);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
