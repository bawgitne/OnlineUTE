package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.CourseDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Course;

import java.util.List;

public class CourseDAOImpl extends AbstractDAO<Course> implements CourseDAO {
    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    public Course save(Course course) {
        return saveEntity(course);
    }

    @Override
    public Course update(Course course) {
        return saveEntity(course);
    }

    @Override
    public Course delete(Course course) {
        if (course == null) {
            return null;
        }
        deleteEntityById(course.getId());
        return course;
    }

    @Override
    public Course findById(Long id) {
        return findEntityById(id);
    }

    @Override
    public List<Course> findAll() {
        return executeRead(em -> em.createQuery("SELECT c FROM Course c ORDER BY c.courseCode", Course.class)
                .getResultList());
    }

    @Override
    public PagedResult<Course> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Course> items = em.createQuery(
                            "SELECT c FROM Course c " +
                                    "WHERE LOWER(c.courseCode) LIKE :keyword " +
                                    "OR LOWER(c.fullName) LIKE :keyword " +
                                    "ORDER BY c.courseCode",
                            Course.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(c) FROM Course c " +
                                    "WHERE LOWER(c.courseCode) LIKE :keyword " +
                                    "OR LOWER(c.fullName) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        });
    }

    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(c) FROM Course c", Long.class)
                .getSingleResult());
    }
}