package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.util.List;
import java.util.Optional;

public class StudentDAOImpl extends AbstractDAO<Student> implements StudentDAO {
    public StudentDAOImpl() {
        super(Student.class);
    }

    // lưu sv mới hoặc update cũ
    @Override
    public Student save(Student student) {
        return saveEntity(student);
    }

    // tìm sv theo id, có fetch luôn lớp/khoa cho đỡ tốn query lẻ
    @Override
    public Optional<Student> findById(Long id) {
        return executeRead(em -> {
            Student student = em.createQuery(
                            "SELECT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.major m " +
                                    "LEFT JOIN FETCH m.faculty " +
                                    "WHERE s.id = :id",
                            Student.class
                    )
                    .setParameter("id", id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(student);
        });
    }

    // tìm sv dựa trên id của account login
    @Override
    public Optional<Student> findByAccountId(Long accountId) {
        return executeRead(em -> {
            Student student = em.createQuery(
                            "SELECT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.major m " +
                                    "LEFT JOIN FETCH m.faculty " +
                                    "WHERE s.account.id = :accountId",
                            Student.class
                    )
                    .setParameter("accountId", accountId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(student);
        });
    }

    // lấy hết danh sách sv kèm thông tin lớp khoa
    @Override
    public List<Student> findAll() {
        return executeRead(em -> em.createQuery(
                        "SELECT s FROM Student s " +
                                "LEFT JOIN FETCH s.classEntity c " +
                                "LEFT JOIN FETCH c.major m " +
                                "LEFT JOIN FETCH m.faculty " +
                                "ORDER BY s.fullName, s.code",
                        Student.class
                ).getResultList());
    }

    // search sv theo đủ thứ: tên, mã, mail, lớp... có phân trang
    @Override
    public PagedResult<Student> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Student> items = em.createQuery(
                            "SELECT DISTINCT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.major m " +
                                    "LEFT JOIN FETCH m.faculty f " +
                                    "WHERE LOWER(s.code) LIKE :keyword " +
                                    "OR LOWER(s.fullName) LIKE :keyword " +
                                    "OR LOWER(s.email) LIKE :keyword " +
                                    "OR LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "ORDER BY s.fullName, s.code",
                            Student.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(DISTINCT s.id) FROM Student s " +
                                    "LEFT JOIN s.classEntity c " +
                                    "LEFT JOIN c.major m " +
                                    "LEFT JOIN m.faculty f " +
                                    "WHERE LOWER(s.code) LIKE :keyword " +
                                    "OR LOWER(s.fullName) LIKE :keyword " +
                                    "OR LOWER(s.email) LIKE :keyword " +
                                    "OR LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(m.fullName) LIKE :keyword " +
                                    "OR LOWER(m.majorCode) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        });
    }

    // tổng số sv trong db
    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(s) FROM Student s", Long.class)
                .getSingleResult());
    }

    // xóa sv theo id
    @Override
    public void deleteById(Long id) {
        deleteEntityById(id);
    }

    // đếm sv theo đầu số mã sv (để sinh mã mới)
    @Override
    public long countByCodePrefix(String codePrefix) {
        return executeRead(em -> em.createQuery(
                        "SELECT COUNT(s) FROM Student s WHERE s.code LIKE :codePrefix",
                        Long.class
                )
                .setParameter("codePrefix", codePrefix + "%")
                .getSingleResult());
    }
}