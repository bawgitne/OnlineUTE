package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.Student;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public Student save(Student student) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (student.getId() == null) {
                em.persist(student);
            } else {
                student = em.merge(student);
            }
            em.getTransaction().commit();
            return student;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Student> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Student student = em.createQuery("SELECT s FROM Student s WHERE s.account.id = :accountId", Student.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(student);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
