package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.MajorDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public class MajorDAOImpl extends AbstractDAO<Major> implements MajorDAO {
    public MajorDAOImpl() {
        super(Major.class);
    }

    // lấy hết các ngành học kèm thông tin khoa
    @Override
    public List<Major> findAll() {
        return executeRead(em -> em.createQuery(
                        "SELECT m FROM Major m JOIN FETCH m.faculty ORDER BY m.fullName",
                        Major.class
                ).getResultList());
    }

    // search ngành theo tên, mã hoặc tên khoa có phân trang
    @Override
    public PagedResult<Major> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Major> items = em.createQuery(
                            "SELECT m FROM Major m JOIN FETCH m.faculty f " +
                                    "WHERE LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword " +
                                    "ORDER BY m.fullName",
                            Major.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(m) FROM Major m JOIN m.faculty f " +
                                    "WHERE LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        });
    }

    // lấy các ngành thuộc về 1 khoa nào đó
    @Override
    public List<Major> findByFacultyId(Long facultyId) {
        return executeRead(em -> em.createQuery(
                        "SELECT m FROM Major m JOIN FETCH m.faculty f WHERE f.id = :facultyId ORDER BY m.fullName",
                        Major.class
                )
                .setParameter("facultyId", facultyId)
                .getResultList());
    }

    // lưu ngành mới hoặc cập nhật ngành cũ
    @Override
    public Major save(Major major) {
        return saveEntity(major);
    }

    // xóa ngành theo id
    @Override
    public void deleteById(Long id) {
        deleteEntityById(id);
    }

    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(m) FROM Major m", Long.class)
                .getSingleResult());
    }
}