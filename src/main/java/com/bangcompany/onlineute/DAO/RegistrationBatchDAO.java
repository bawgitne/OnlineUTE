package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrationBatchDAO {
    RegistrationBatch save(RegistrationBatch registrationBatch);
    Optional<RegistrationBatch> findById(Long id);
    List<RegistrationBatch> findByTermId(Long termId);
    List<RegistrationBatch> findAll();
    List<RegistrationBatch> findOpenBatches(LocalDateTime currentTime);
}
