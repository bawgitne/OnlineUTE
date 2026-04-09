package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.FacultyDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Faculty;

import java.util.List;

public class FacultyDAOImpl extends AbstractDAO<Faculty> implements FacultyDAO {
    public FacultyDAOImpl() {
        super(Faculty.class);
    }

    // lấy hết danh sách khoa
    @Override
    public List<Faculty> findAll() {
        return executeRead(em -> em.createQuery("SELECT f FROM Faculty f ORDER BY f.fullName", Faculty.class)
                .getResultList());
    }

    // tìm khoa theo tên hoặc mã khoa có phân trang
    @Override
    public PagedResult<Faculty> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Faculty> items = em.createQuery(
                            "SELECT f FROM Faculty f " +
                                    "WHERE LOWER(f.facultyCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "ORDER BY f.fullName",
                            Faculty.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(f) FROM Faculty f " +
                                    "WHERE LOWER(f.facultyCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        });
    }

    // lưu khoa mới hoặc cập nhật khoa cũ
    @Override
    public Faculty save(Faculty faculty) {
        return saveEntity(faculty);
    }

    // xóa khoa theo id
    @Override
    public void deleteById(Long id) {
        deleteEntityById(id);
    }

    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(f) FROM Faculty f", Long.class)
                .getSingleResult());
    }
}