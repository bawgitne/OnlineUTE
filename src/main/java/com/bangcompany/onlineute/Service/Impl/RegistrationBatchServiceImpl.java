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

    // tạo mới đợt đăng ký môn học
    @Override
    public RegistrationBatch createBatch(RegistrationBatch registrationBatch) {
        validateBatch(registrationBatch);
        return registrationBatchDAO.save(registrationBatch);
    }

    // cập nhật thông tin đợt đăng ký đã tồn tại
    @Override
    public RegistrationBatch updateBatch(RegistrationBatch registrationBatch) {
        validateBatch(registrationBatch);
        return registrationBatchDAO.save(registrationBatch);
    }

    // lấy thông tin đợt đăng ký theo ID
    @Override
    public Optional<RegistrationBatch> getBatchById(Long id) {
        return registrationBatchDAO.findById(id);
    }

    // lấy danh sách đợt đăng ký thuộc học kỳ cụ thể
    @Override
    public List<RegistrationBatch> getBatchesByTerm(Long termId) {
        return registrationBatchDAO.findByTermId(termId);
    }

    // lấy toàn bộ danh sách các đợt đăng ký
    @Override
    public List<RegistrationBatch> getAllBatches() {
        return registrationBatchDAO.findAll();
    }

    // lấy các đợt đăng ký đang trong thời gian mở
    @Override
    public List<RegistrationBatch> getOpenBatches(LocalDateTime currentTime) {
        return registrationBatchDAO.findOpenBatches(currentTime);
    }

    // kiểm tra tính hợp lệ của dữ liệu đợt đăng ký trước khi lưu
    private void validateBatch(RegistrationBatch registrationBatch) {
        if (registrationBatch == null) {
            throw new IllegalArgumentException("Đợt đăng ký không được để trống.");
        }
        if (registrationBatch.getName() == null || registrationBatch.getName().isBlank()) {
            throw new IllegalArgumentException("Tên đợt đăng ký không được để trống.");
        }
        if (registrationBatch.getTerm() == null) {
            throw new IllegalArgumentException("Học kỳ áp dụng không được để trống.");
        }
        if (registrationBatch.getCommonStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu học chung không được để trống.");
        }
        if (registrationBatch.getOpenAt() == null || registrationBatch.getCloseAt() == null) {
            throw new IllegalArgumentException("Thời gian mở và đóng đăng ký không được để trống.");
        }
        // Đảm bảo logic thời gian mở phải luôn trước thời gian đóng
        if (!registrationBatch.getOpenAt().isBefore(registrationBatch.getCloseAt())) {
            throw new IllegalArgumentException("Thời gian mở đăng ký phải trước thời gian đóng đăng ký.");
        }
    }
}
