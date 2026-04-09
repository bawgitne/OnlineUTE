package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Class;

import java.util.List;

public class ClassDAOImpl extends AbstractDAO<Class> implements ClassDAO {
    public ClassDAOImpl() {
        super(Class.class);
    }

    // lấy hết list lớp kèm ngành và khoa
    @Override
    public List<Class> findAll() {
        return executeRead(em -> em.createQuery(
                        "SELECT c FROM Class c JOIN FETCH c.major m JOIN FETCH m.faculty ORDER BY c.className",
                        Class.class
                ).getResultList());
    }

    // lấy các lớp của 1 ngành học nhất định
    @Override
    public List<Class> findByMajorId(Long majorId) {
        return executeRead(em -> em.createQuery(
                        "SELECT c FROM Class c JOIN FETCH c.major m JOIN FETCH m.faculty f WHERE m.id = :majorId ORDER BY c.className",
                        Class.class
                )
                .setParameter("majorId", majorId)
                .getResultList());
    }

    // search lớp theo tên, ngành, khoa... có phân trang
    @Override
    public PagedResult<Class> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
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
        });
    }

    // nạp lớp mới hoặc update cũ vào db
    @Override
    public Class save(Class classEntity) {
        return saveEntity(classEntity);
    }

    // xóa lớp theo id
    @Override
    public void deleteById(Long id) {
        deleteEntityById(id);
    }

    // đếm tổng số lớp hành chính hiện có
    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(c) FROM Class c", Long.class)
                .getSingleResult());
    }
}