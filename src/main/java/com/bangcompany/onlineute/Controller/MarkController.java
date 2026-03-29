package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Service.MarkService;
import java.util.Optional;

public class MarkController {
    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    public Mark updateMark(Mark mark) {
        return markService.saveMark(mark);
    }

    public Optional<Mark> getMarkByRegistration(Long registrationId) {
        return markService.getMarkByRegistration(registrationId);
    }

    public Optional<Mark> getMarkById(Long id) {
        return markService.getMarkById(id);
    }
}
