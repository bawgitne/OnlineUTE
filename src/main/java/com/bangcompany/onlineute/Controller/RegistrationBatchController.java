package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Service.RegistrationBatchService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RegistrationBatchController {
    private final RegistrationBatchService registrationBatchService;

    public RegistrationBatchController(RegistrationBatchService registrationBatchService) {
        this.registrationBatchService = registrationBatchService;
    }

    public RegistrationBatch createBatch(RegistrationBatch registrationBatch) {
        return registrationBatchService.createBatch(registrationBatch);
    }

    public RegistrationBatch updateBatch(RegistrationBatch registrationBatch) {
        return registrationBatchService.updateBatch(registrationBatch);
    }

    public Optional<RegistrationBatch> getBatchById(Long id) {
        return registrationBatchService.getBatchById(id);
    }

    public List<RegistrationBatch> getBatchesByTerm(Long termId) {
        return registrationBatchService.getBatchesByTerm(termId);
    }

    public List<RegistrationBatch> getAllBatches() {
        return registrationBatchService.getAllBatches();
    }

    public List<RegistrationBatch> getOpenBatches(LocalDateTime currentTime) {
        return registrationBatchService.getOpenBatches(currentTime);
    }
}
