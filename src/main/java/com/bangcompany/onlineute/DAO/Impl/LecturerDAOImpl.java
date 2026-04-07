
package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class LecturerDAOImpl implements LecturerDAO {

    @Override
    public Lecturer save(Lecturer lecturer) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (lecturer.getId() == null) {
                // Thêm mới giảng viên
                em.persist(lecturer);
            } else {
                // Cập nhật thông tin giảng viên
                lecturer = em.merge(lecturer);
            }
            em.getTransaction().commit();
            return lecturer;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lecturer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy toàn bộ danh sách giảng viên sắp xếp theo tên
            return em.createQuery("SELECT l FROM Lecturer l ORDER BY l.fullName", Lecturer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Lecturer> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Tìm giảng viên theo ID của tài khoản
            Lecturer lecturer = em.createQuery("SELECT l FROM Lecturer l WHERE l.account.id = :accountId", Lecturer.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(lecturer);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Lecturer> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            // Tìm giảng viên theo mã hoặc họ tên (kèm phân trang)
            List<Lecturer> items = em.createQuery(
                            "SELECT DISTINCT l FROM Lecturer l " +
                                    "WHERE LOWER(l.code) LIKE :keyword " +
                                    "OR LOWER(l.fullName) LIKE :keyword " +
                                    "ORDER BY l.fullName, l.code",
                            Lecturer.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            // Đếm tổng số lượng giảng viên tìm được
            Long totalItems = em.createQuery(
                            "SELECT COUNT(DISTINCT l.id) FROM Lecturer l " +
                                    "WHERE LOWER(l.code) LIKE :keyword " +
                                    "OR LOWER(l.fullName) LIKE :keyword",
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
    public long countAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Tổng số lượng giảng viên trong hệ thống
            return em.createQuery("SELECT COUNT(l) FROM Lecturer l", Long.class)
                    .getSingleResult();
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
            Lecturer lecturer = em.find(Lecturer.class, id);
            if (lecturer != null) {
                // Xóa giảng viên nếu tồn tại
                em.remove(lecturer);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
