package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrationBatchService {
    RegistrationBatch createBatch(RegistrationBatch registrationBatch);
    RegistrationBatch updateBatch(RegistrationBatch registrationBatch);
    Optional<RegistrationBatch> getBatchById(Long id);
    List<RegistrationBatch> getBatchesByTerm(Long termId);
    List<RegistrationBatch> getAllBatches();
    List<RegistrationBatch> getOpenBatches(LocalDateTime currentTime);
}
