package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.MarkDAO;
import com.bangcompany.onlineute.Model.Entity.Mark;
import java.util.Optional;

public class MarkDAOImpl extends AbstractDAO<Mark> implements MarkDAO {
    public MarkDAOImpl() {
        super(Mark.class);
    }

    // lưu điểm (tạo mới/update)
    @Override
    public Mark save(Mark mark) {
        return saveEntity(mark);
    }

    @Override
    public Mark update(Mark mark) {
        return saveEntity(mark);
    }

    // xóa điểm khỏi db
    @Override
    public void delete(Mark mark) {
        if (mark == null) {
            return;
        }
        deleteEntityById(mark.getId());
    }

    // tìm điểm theo id chính của bảng mark
    @Override
    public Optional<Mark> findById(Long id) {
        return Optional.ofNullable(findEntityById(id));
    }

    // lấy điểm của 1 dòng đăng ký môn tương ứng
    @Override
    public Optional<Mark> findByRegistrationId(Long registrationId) {
        return executeRead(em -> em.createQuery(
                        "SELECT m FROM Mark m WHERE m.courseRegistration.id = :registrationId",
                        Mark.class)
                .setParameter("registrationId", registrationId)
                .getResultStream()
                .findFirst());
    }
}