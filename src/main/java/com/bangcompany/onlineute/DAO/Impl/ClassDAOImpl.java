
package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Class;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClassDAOImpl implements ClassDAO {
    @Override
    public List<Class> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy danh sách tất cả các lớp kèm thông tin ngành và khoa
            return em.createQuery(
                    "SELECT c FROM Class c JOIN FETCH c.major m JOIN FETCH m.faculty ORDER BY c.className",
                    Class.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Class> findByMajorId(Long majorId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Tìm danh sách lớp theo ID của ngành
            return em.createQuery(
                    "SELECT c FROM Class c JOIN FETCH c.major m JOIN FETCH m.faculty f WHERE m.id = :majorId ORDER BY c.className",
                    Class.class
            ).setParameter("majorId", majorId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Class> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            // Tìm kiếm lớp theo tên lớp, tên ngành, mã ngành hoặc tên khoa
            List<Class> items = em.createQuery(
                            "SELECT c FROM Class c JOIN FETCH c.major m JOIN FETCH m.faculty f " +
                                    "WHERE LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword " +
                                    "ORDER BY c.className",
                            Class.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            // Đếm tổng số lượng kết quả tìm được
            Long totalItems = em.createQuery(
                            "SELECT COUNT(c) FROM Class c JOIN c.major m JOIN m.faculty f " +
                                    "WHERE LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        } finally {
            em.close();
        }
    }

    @Override
    public Class save(Class classEntity) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (classEntity.getId() == null) {
                // Thêm mới nếu chưa có ID
                em.persist(classEntity);
            } else {
                // Cập nhật nếu đã có ID
                classEntity = em.merge(classEntity);
            }
            em.getTransaction().commit();
            return classEntity;
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
            Class classEntity = em.find(Class.class, id);
            if (classEntity != null) {
                // Xóa lớp nếu tồn tại
                em.remove(classEntity);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
