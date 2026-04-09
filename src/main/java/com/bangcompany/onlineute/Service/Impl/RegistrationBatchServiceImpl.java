package com.bangcompany.onlineute.Service.Impl;


import com.bangcompany.onlineute.DAO.RegistrationBatchDAO;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Service.RegistrationBatchService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RegistrationBatchServiceImpl implements RegistrationBatchService {
    private final RegistrationBatchDAO registrationBatchDAO;

    public RegistrationBatchServiceImpl(RegistrationBatchDAO registrationBatchDAO) {
        this.registrationBatchDAO = registrationBatchDAO;
    }

    // tạo đợt đăng ký môn mới
    @Override
    public RegistrationBatch createBatch(RegistrationBatch registrationBatch) {
        validateBatch(registrationBatch);
        return registrationBatchDAO.save(registrationBatch);
    }

    // lưu thay đổi cho đợt đăng ký
    @Override
    public RegistrationBatch updateBatch(RegistrationBatch registrationBatch) {
        validateBatch(registrationBatch);
        return registrationBatchDAO.save(registrationBatch);
    }

    // tìm đợt đk theo id
    @Override
    public Optional<RegistrationBatch> getBatchById(Long id) {
        return registrationBatchDAO.findById(id);
    }

    // lấy đợt đk của học kỳ nào đó
    @Override
    public List<RegistrationBatch> getBatchesByTerm(Long termId) {
        return registrationBatchDAO.findByTermId(termId);
    }

    // lấy hết danh sách đợt đk
    @Override
    public List<RegistrationBatch> getAllBatches() {
        return registrationBatchDAO.findAll();
    }

    // tìm mấy đợt đang trong giờ mở cửa cho sv đk
    @Override
    public List<RegistrationBatch> getOpenBatches(LocalDateTime currentTime) {
        return registrationBatchDAO.findOpenBatches(currentTime);
    }

    private void validateBatch(RegistrationBatch registrationBatch) {
        if (registrationBatch == null) {
            throw new IllegalArgumentException("Ã„Â ợt đÃ„Æ’ng ký không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        if (registrationBatch.getName() == null || registrationBatch.getName().isBlank()) {
            throw new IllegalArgumentException("Tên đợt đăng ký không được để trống.");
        }
        if (registrationBatch.getTerm() == null) {
            throw new IllegalArgumentException("Họ c kọ³ áp dọ¥ng không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        if (registrationBatch.getCommonStartDate() == null) {
            throw new IllegalArgumentException("NgÃƒÂ y bắt đầu họ c chung không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        if (registrationBatch.getOpenAt() == null || registrationBatch.getCloseAt() == null) {
            throw new IllegalArgumentException("Thọ i gian máÂ»Å¸ và đóng đÃ„Æ’ng ký không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        // check giờ mở phải trước giờ đóng
        if (!registrationBatch.getOpenAt().isBefore(registrationBatch.getCloseAt())) {
            throw new IllegalArgumentException("Thọ i gian máÂ»Å¸ đÃ„Æ’ng ký phải trÃ†Â°ớic thọ i gian đóng đÃ„Æ’ng ký.");
        }
    }
}
